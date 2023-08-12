import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../models/author.dart';
import '../models/book.dart';
import '../services/api_service.dart';


class AuthorProvider extends ChangeNotifier {
  List<Author> authors = [];
  List<Book> authorBooks = [];
  Author? authorToEdit;
  int pagesNumber = 0;

  static const String authorEndpoint = "api/authors";
  final ApiService _apiService = ApiService();

  getAuthors(int pageNumber, int pageSize, String pageType, BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.getAllPaged(authorEndpoint, "getAllAuthorsPaged", pageNumber, pageSize, prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is Map<String, dynamic>) {
      final rawAuthors = response['content'] as List;
      List<Author> listAuthors = rawAuthors.map((json) => Author.fromJson(json)).toList();
      final int pagesValue = response['pageable']['totalPages'];

      if (pageType == "PAGING") {authors = authors + listAuthors;}
      else {authors = listAuthors;}
      pagesNumber = pagesValue;
      notifyListeners();
    }
  }

  getAuthorInfo(Author author, BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.get("$authorEndpoint/${author.id}", "getAuthorInfo", prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is Map<String, dynamic>) {
      response = Author.fromJson(response);
      authorBooks = response.books!;
      notifyListeners();
    }
  }

  dynamic addAuthor(Author author, BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.insert(authorEndpoint, "addAuthor", author.toJson(author), prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is Map<String, dynamic>) {
      response = Author.fromJson(response);
      authors.add(response);
      notifyListeners();
      if (context.mounted) {
        _showSnackBar(context, "Success!");
        return Navigator.of(context).pop();
      }
    }
  }

  dynamic editAuthor(Author author, BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.update("$authorEndpoint/${author.id}", "editAuthor", author.toJson(author), prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is Map<String, dynamic>) {
      response = Author.fromJson(response);
      authors[authors.indexOf(author)] = response;
      notifyListeners();
      if (context.mounted) {
        _showSnackBar(context, "Success!");
        return Navigator.of(context).pop();
      }
    }
  }

  dynamic deleteAuthor(Author author, BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.delete("$authorEndpoint/${author.id}", "deleteAuthor", prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is bool) {
      authors.remove(author);
      notifyListeners();
      if (context.mounted) {
        _showSnackBar(context, "Success!");
        return Navigator.of(context).pop();
      }
    }
  }

  _showSnackBar(BuildContext context, String msg) {
    SnackBar snackBar = SnackBar(content: Text(msg));
    if (context.mounted) ScaffoldMessenger.of(context).showSnackBar(snackBar);
  }
}
