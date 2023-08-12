import 'package:books_collection/models/book.dart';
import 'package:books_collection/services/api_service.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class BookProvider extends ChangeNotifier {
  List<Book> books = [];
  Book? bookToEdit;
  int pagesNumber = 0;

  static const String bookEndpoint = "api/books";
  final ApiService _apiService = ApiService();

  getBooks(int pageNumber, int pageSize, String pageType, BuildContext context, SharedPreferences prefs, {title=''}) async {
    dynamic response = await _apiService.getAllPaged(bookEndpoint, "getAllBooksPaged", pageNumber, pageSize, prefs, otherParam: title);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is Map<String, dynamic>) {
      final rawBooks = response['content'] as List;
      List<Book> listBooks = rawBooks.map((json) => Book.fromJson(json)).toList();
      final int pagesValue = response['pageable']['totalPages'];

      if (pageType == "PAGING") {books = books + listBooks;}
      else {books = listBooks;}
      pagesNumber = pagesValue;
      notifyListeners();
    }
  }

  dynamic addBook(Book book, FilePickerResult? filePickerResult, BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.insert(bookEndpoint, "addBook", book.toJson(book), prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is Map<String, dynamic>) {
      response = Book.fromJson(response);
      if(filePickerResult != null) {
        _apiService.insertFile(filePickerResult, response, prefs);
      }
      books.add(response);
      notifyListeners();
      if (context.mounted) {
        _showSnackBar(context, "Success!");
        return Navigator.of(context).pop();
      }
    }
  }

  dynamic editBook(Book book, FilePickerResult? filePickerResult, BuildContext context, SharedPreferences prefs) async {
    if(filePickerResult != null) {
      book.bookUrl = null;
      book.coverUrl = null;
    }
    dynamic response = await _apiService.update("$bookEndpoint/${book.id}", "editBook", book.toJson(book), prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is Map<String, dynamic>) {
      response = Book.fromJson(response);
      if(filePickerResult != null) {
        _apiService.insertFile(filePickerResult, book, prefs);
      }
      books[books.indexOf(book)] = response;
      notifyListeners();
      if (context.mounted) {
        _showSnackBar(context, "Success!");
        return Navigator.of(context).pop();
      }
    }
  }

  dynamic deleteBook(Book book, BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.delete("$bookEndpoint/${book.id}", "deleteBook", prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is bool) {
      books.remove(book);
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
