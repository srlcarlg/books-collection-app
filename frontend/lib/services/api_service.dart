
import 'package:dio/dio.dart';
import 'package:file_picker/file_picker.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';

import '../models/book.dart';
import 'package:books_collection/config.dart';
class ApiService {
  String url = serverUrl;

  String filesURL = "$serverUrl/files";

  // ====== Core =====
  Future<dynamic> getAllPaged(String endpoint, String name, int pageNumber, int pageSize, SharedPreferences prefs, {otherParam=''}) async {
    String? token = prefs.getString("access");
    http.Response response;
    Map<String, String> params = {
      'page': pageNumber.toString(),
      'size': pageSize.toString(),
      'sort': "id,asc"};
    if (otherParam != '') params.addAll({'title': otherParam});

    try {
      response = await http.get(Uri.http(url, "/$endpoint", params),
        headers: {
          "Authorization": "Bearer $token"
        },
      );
    } catch (e) {
      return "Something wrong when calling the API. It's ON? '$e'";
    }

    int status = response.statusCode;
    var jsonResponse = jsonDecode(response.body);
    if (status != 200) {
      if (status == 401) return "Unauthorized or Expired Token. Try re-authenticate";
      return "Unexpected error in $name. '$status' code returned from API";
    }

    return jsonResponse;
  }
  Future<dynamic> get(String endpoint, String name, SharedPreferences prefs) async {
    String? token = prefs.getString("access");
    http.Response response;
    try {
      response = await http.get(Uri.http(url, "/$endpoint"),
        headers: {
          "Authorization": "Bearer $token"
        },
      );
    } catch (e) {
      return "Something wrong when calling the API. It's ON? '$e'";
    }

    int status = response.statusCode;
    var jsonResponse = jsonDecode(response.body);
    if (status != 200) {
      if (status == 401) return "Unauthorized or Expired Token. Try re-authenticate";
      return "Unexpected error in FindAuthorInfo. '$status' code returned from API";
    }

    return jsonResponse;
  }
  Future<dynamic> insert(String endpoint, String name, Map<String, dynamic> body, SharedPreferences prefs, {isPublic=false}) async {
    String? token = prefs.getString("access");
    http.Response response;
    try {
      Map<String, String> head = {"Content-Type": "application/json"};
      if (!isPublic) {head.addAll({"Authorization": "Bearer $token"});}
      response = await http.post(Uri.http(url, "/$endpoint"),
          headers: head,
          body: json.encode(body)
      );
    } catch (e) {
      return "Something wrong when calling the API. It's ON? '$e'";
    }

    int status = response.statusCode;
    if (status == 205) return "";
    var jsonResponse = jsonDecode(response.body);
    if (status != 200) {
      if (status == 401) return "Unauthorized or Expired Token. Try re-authenticate";
      if (status == 422) return validationResponse(jsonResponse);
      return "Unexpected error in $name. '$status' code returned from API";
    }

    return jsonResponse;
  }
  Future<dynamic> update(String endpoint, String name, Map<String, dynamic> body, SharedPreferences prefs) async {
    String? token = prefs.getString("access");
    http.Response response;
    try {
      response = await http.put(Uri.http(url, "/$endpoint"),
          headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer $token"
          },
          body: json.encode(body)
      );
    } catch (e) {
      return "Something wrong when calling the API. It's ON? '$e'";
    }

    int status = response.statusCode;
    var jsonResponse = jsonDecode(response.body);
    if (status != 200) {
      if (status == 401) return "Unauthorized or Expired Token. Try re-authenticate";
      if (status == 422) return validationResponse(jsonResponse);
      return "Unexpected error in $name. '$status' code returned from API";
    }

    return jsonResponse;
  }
  Future<dynamic> delete(String endpoint, String name, SharedPreferences prefs) async {
    String? token = prefs.getString("access");
    http.Response response;
    try {
      response = await http.delete(Uri.http(url, "/$endpoint"),
          headers: {
            "Authorization": "Bearer $token"
          }
      );
    } catch (e) {
      return "Something wrong when calling the API. It's ON? '$e'";
    }

    int status = response.statusCode;
    if (status != 204) {
      var jsonResponse = jsonDecode(response.body);
      if (status == 401) return "Unauthorized or Expired Token. Try re-authenticate";
      if (status == 400) return jsonResponse["message"];
      return "Unexpected error in $name. '$status' code returned from API";
    }
    return true;
  }

  // ====== Public =====
  Future<dynamic> authenticate(String email, String password, SharedPreferences prefs) async {

    Map<String, dynamic> body = {
      "email":email,
      "password":password
    };
    dynamic response = await insert("auth/token", "authenticate", body, prefs, isPublic: true);
    if (response is String) {
      return response;
    }
    prefs.setString('access', response['access']);
    prefs.setString('refresh', response['refresh']);
    me(prefs);

    return true;
  }
  Future<dynamic> reAuthenticate(SharedPreferences prefs) async {

    String? refresh = prefs.getString('refresh');
    Map<String, dynamic> body = {
      "refresh":refresh,
    };
    dynamic response = await insert("auth/refresh", "authenticate", body, prefs, isPublic: true);
    if (response is String) {
      return response;
    }

    prefs.setString('access', response['access']);
    prefs.setString('refresh', response['refresh']);
    return true;
  }
  Future<dynamic> logout(SharedPreferences prefs) async {

    String? refresh = prefs.getString('refresh');
    Map<String, dynamic> body = {
      "refresh":refresh,
    };
    dynamic response = await insert("auth/logout", "authenticate", body, prefs, isPublic: true);
    if (response is String) {
      return response;
    }
    return true;
  }
  // Info
  Future<String> me(SharedPreferences prefs) async {
    dynamic response = await get("api/me", "ME", prefs);
    if (response is String) {
      return response;
    }
    prefs.setString('name', response['name']);
    prefs.setString('typeUser', response['type_user']);
    prefs.setString('email', response['email']);
    return "Successful!";
  }

  // ====== Storage/Files =====
  Future<dynamic> insertFile(FilePickerResult filePickerResult, Book book, SharedPreferences prefs) async {

    String? token = prefs.getString("access");

    PlatformFile file = filePickerResult.files.first;
    FormData data = FormData.fromMap({
      "file": await MultipartFile.fromFile(
        file.path!,
        filename: file.name,
      )
    });
    int? bookId = book.id;
    final Dio dio = Dio();
    dynamic response;
    try {
      response = await dio.post("http://$filesURL/upload/$bookId",
        data: data,
        options: Options(
          headers: {
            "Authorization": "Bearer $token"
          },
        )
      );
    } catch (e) {
      return "Something wrong when calling the API. It's ON? '$e'";
    }

    int status = response.statusCode;
    if (status != 200) {
      if (status == 401) return "Unauthorized or Expired Token. Try re-authenticate";
      return "Unexpected error in insertFile. '$status' code returned from API";
    }

  }

  static String validationResponse(var jsonResponse) {
    String field;
    String message;
    try {
      field = jsonResponse['errors'][0]['fieldName'];
      message = jsonResponse['errors'][0]['message'];
    } catch (e) {
      return jsonResponse.toString().replaceAll("{", "").replaceAll("}", "");
    }
    return "$field : $message";
  }
}