import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../models/category.dart';
import '../models/book.dart';
import '../services/api_service.dart';


class CategoryProvider extends ChangeNotifier {
  List<Category> categories = [];
  List<Book> categoryBooks = [];
  Category? categoryToEdit;
  int pagesNumber = 0;

  static const String categoryEndpoint = "api/categories";
  final ApiService _apiService = ApiService();

  getCategories(int pageNumber, int pageSize, String pageType, BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.getAllPaged(categoryEndpoint, "getAllCategoriesPaged", pageNumber, pageSize, prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is Map<String, dynamic>) {
      final rawCategories = response['content'] as List;
      List<Category> listCategories = rawCategories.map((json) => Category.fromJson(json)).toList();
      final int pagesValue = response['pageable']['totalPages'];

      if (pageType == "PAGING") {categories = categories + listCategories;}
      else {categories = listCategories;}
      pagesNumber = pagesValue;
      notifyListeners();
    }
  }

  getCategoryInfo(Category category, BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.get("$categoryEndpoint/${category.id}", "getCategoryInfo", prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is Map<String, dynamic>) {
      response = Category.fromJson(response);
      categoryBooks = response.books!;
      notifyListeners();
    }
  }

  dynamic addCategory(Category category, BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.insert(categoryEndpoint, "addCategory", category.toJson(category), prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is Map<String, dynamic>) {
      response = Category.fromJson(response);
      categories.add(response);
      notifyListeners();
      if (context.mounted) {
        _showSnackBar(context, "Success!");
        return Navigator.of(context).pop();
      }
    }
  }

  dynamic editCategory(Category category, BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.update("$categoryEndpoint/${category.id}", "editCategory", category.toJson(category), prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is Map<String, dynamic>) {
      response = Category.fromJson(response);
      categories[categories.indexOf(category)] = response;
      notifyListeners();
      if (context.mounted) {
        _showSnackBar(context, "Success!");
        return Navigator.of(context).pop();
      }
    }
  }

  dynamic deleteCategory(Category category, BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.delete("$categoryEndpoint/${category.id}", "deleteCategory", prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is bool) {
      categories.remove(category);
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
