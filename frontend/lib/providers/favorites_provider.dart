import 'package:books_collection/models/book.dart';
import 'package:books_collection/services/api_service.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class FavoriteProvider extends ChangeNotifier {
  List<Book> favoriteBooks = [];
  int pagesNumber = 0;

  static const String favoriteEndpoint = "api/favorites";
  final ApiService _apiService = ApiService();

  getFavoriteBooks(int pageNumber, int pageSize, String pageType, BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.getAllPaged(favoriteEndpoint, "getAllFavoritesPaged", pageNumber, pageSize, prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is Map<String, dynamic>) {
      final rawBooks = response['content'] as List;
      List<Book> listBooks = rawBooks.map((json) => Book.fromJson(json)).toList();
      final int pagesValue = response['pageable']['totalPages'];

      if (pageType == "PAGING") {favoriteBooks = favoriteBooks + listBooks;}
      else {favoriteBooks = listBooks;}
      pagesNumber = pagesValue;
      notifyListeners();
    }
  }

  dynamic addFavoriteBook(Book book, BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.insert("$favoriteEndpoint/${book.id}", "insertBookFavorite",  {}, prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is Map<String, dynamic>) {
      response = Book.fromJson(response);
      favoriteBooks.add(response);
      notifyListeners();
      if (context.mounted) {
        _showSnackBar(context, "Success!");
        return Navigator.of(context).pop();
      }
    }
  }
  deleteFavoriteBook(Book book, BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.delete("$favoriteEndpoint/${book.id}", "deleteBookFavorite", prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is bool) {
      favoriteBooks.remove(book);
      notifyListeners();
    }
  }
  _showSnackBar(BuildContext context, String msg) {
    SnackBar snackBar = SnackBar(content: Text(msg));
    if (context.mounted) ScaffoldMessenger.of(context).showSnackBar(snackBar);
  }
}
