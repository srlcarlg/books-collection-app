import 'package:books_collection/models/author.dart';
import 'package:books_collection/models/category.dart';

import 'enums/book_status.dart';

class Book {
  int? id;
  String? title;
  int? publicationYear;
  BookStatus? bookStatus;
  String? description;
  late List<Author> authors;
  late List<Category> categories;
  String? bookUrl;
  String? coverUrl;

  Book({
    this.id,
    this.title,
    this.publicationYear,
    this.bookStatus,
    this.description,
    required this.authors,
    required this.categories,
    this.bookUrl,
    this.coverUrl
  });

  Book.empty() {
    bookStatus = BookStatus.missing;
  }

  Book copy() {
    return Book(
      id: id,
      title: title,
      publicationYear: publicationYear,
      bookStatus: bookStatus,
      description: description,
      authors:  authors,
      categories: categories,
      bookUrl: bookUrl,
      coverUrl: coverUrl,
    );
  }

  factory Book.fromJson(Map<String, dynamic> json) {
    return Book(
      id: int.parse(json['id'].toString()),
      title: json['title'],
      publicationYear: int.tryParse(json['publication_year'].toString()),
      bookStatus: BookStatus.values.firstWhere((e) => e.name == json['book_status'].toLowerCase()),
      description: json['description'],
      authors:  json["authors"] != null ? List<Author>.from(json["authors"].map((x) => Author.fromJson(x))) : [],
      categories: json["categories"] != null ? List<Category>.from(json["categories"].map((x) => Category.fromJson(x))) : [],
      bookUrl: json['book_url'],
      coverUrl: json['cover_url'],
    );
  }


  Map<String, dynamic> toJson(Book book) {
    Map<String, dynamic> rawJson = {
      "title": book.title,
      "publication_year": book.publicationYear ?? "",
      "description": book.description,
      "book_status": book.bookStatus!.name.toUpperCase(),
      "authors": List<dynamic>.from(book.authors.map((x) => Author().toJsonID(x))),
      "categories": List<dynamic>.from(book.categories.map((x) => Category().toJsonID(x))),
      "book_url": book.bookUrl,
      "cover_url": book.coverUrl
    };
    rawJson.removeWhere((key, value) => value == "" || value == null);
    return rawJson;
  }
}
