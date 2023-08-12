import 'package:books_collection/providers/author_provider.dart';
import 'package:books_collection/providers/book_provider.dart';
import 'package:books_collection/providers/category_provider.dart';
import 'package:books_collection/providers/favorites_provider.dart';
import 'package:books_collection/providers/user_provider.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'pages/login_page.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MultiProvider(
        providers: [
          ChangeNotifierProvider<BookProvider>(create: (context) => BookProvider()),
          ChangeNotifierProvider<AuthorProvider>(create: (context) => AuthorProvider()),
          ChangeNotifierProvider<CategoryProvider>(create: (context) => CategoryProvider()),
          ChangeNotifierProvider<FavoriteProvider>(create: (context) => FavoriteProvider()),
          ChangeNotifierProvider<UserProvider>(create: (context) => UserProvider()),
        ],
        child: MaterialApp(
          title: 'Books Collection Demo',
          theme: ThemeData(
            colorScheme: ColorScheme.fromSeed(seedColor: Colors.yellowAccent),
            useMaterial3: true,
          ),
          home: const LoginPage(),
        ));
  }
}