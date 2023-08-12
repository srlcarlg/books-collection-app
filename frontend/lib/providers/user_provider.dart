import 'package:books_collection/models/update_user.dart';
import 'package:books_collection/pages/home_page.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../models/insert_user.dart';
import '../services/api_service.dart';


class UserProvider extends ChangeNotifier {
  List<UserInsert> users = [];
  UserInsert? userToEdit;
  int pagesNumber = 0;


  static const String userEndpoint = "api/users";
  final ApiService _apiService = ApiService();

  getUsers(int pageNumber, int pageSize, String pageType, BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.getAllPaged(userEndpoint, "getAllUsersPaged", pageNumber, pageSize, prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is Map<String, dynamic>) {
      final rawUsers = response['content'] as List;
      List<UserInsert> listUsers = rawUsers.map((json) => UserInsert.fromJson(json)).toList();
      final int pagesValue = response['pageable']['totalPages'];

      if (pageType == "PAGING") {users = users + listUsers;}
      else {users = listUsers;}
      pagesNumber = pagesValue;
      notifyListeners();
    }
  }

  getUserInfo(UserInsert user, BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.get(userEndpoint, "getAllUsersPaged", prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is Map<String, dynamic>) {
      response = UserInsert.fromJson(response);
      notifyListeners();
    }
  }

  dynamic addUser(UserInsert user, BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.insert(userEndpoint, "addUser", user.toJson(user), prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is Map<String, dynamic>) {
      response = UserInsert.fromJson(response);
      users.add(response);
      notifyListeners();
      if (context.mounted) {
        _showSnackBar(context, "Success!");
        return Navigator.of(context).pop();
      }
    }
  }

  dynamic updateAuthenticatedUser(UserUpdate user, BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.update(userEndpoint, "updateAuthenticatedUser", user.toJson(user), prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is Map<String, dynamic>) {
      await _apiService.me(prefs);
      notifyListeners();
      if (context.mounted) {
        _showSnackBar(context, "Success!");
        return Navigator.of(context).pop();
      }
    }
  }

  dynamic updateUserById(UserInsert user, BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.update("$userEndpoint/${user.id}", "updateUserById", user.toJson(user), prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is Map<String, dynamic>) {
      notifyListeners();
      if (context.mounted) {
        _showSnackBar(context, "Success!");
        return Navigator.of(context).pop();
      }
    }
  }

  dynamic deleteUser(UserInsert user, BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.delete("$userEndpoint/${user.id}", "deleteUser", prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is bool) {
      users.remove(user);
      notifyListeners();
      if (context.mounted) {
        _showSnackBar(context, "Success!");
        return Navigator.of(context).pop();
      }
    }
  }

  dynamic register(UserInsert user, BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.insert(userEndpoint, "addUser", user.toJson(user), prefs, isPublic: true);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is Map<String, dynamic>) {
      response = UserInsert.fromJson(response);
      notifyListeners();
      if (context.mounted) {
        _showSnackBar(context, "Success!");
        return login(user.email!, user.password!, context, prefs);
      }
    }
  }
  login(String email, String password, BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.authenticate(email, password, prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is bool) {
      notifyListeners();
      if (context.mounted) {
        _showSnackBar(context, "Successful");
        return Navigator.push(context, MaterialPageRoute(builder: (context) => const HomePage()));
      }
    }
  }
  reAuthenticate(BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.reAuthenticate(prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is bool) {
      notifyListeners();
      if (context.mounted) {
        _showSnackBar(context, "Successful re-authentication!");
        return Navigator.of(context).pop();
      }
    }
  }
  logout(BuildContext context, SharedPreferences prefs) async {
    dynamic response = await _apiService.logout(prefs);
    if (response is String && context.mounted) _showSnackBar(context, response);
    if (response is bool) {
      notifyListeners();
    }
  }

  _showSnackBar(BuildContext context, String msg) {
    SnackBar snackBar = SnackBar(content: Text(msg,textAlign: TextAlign.end,));
    if (context.mounted) ScaffoldMessenger.of(context).showSnackBar(snackBar);
  }
}
