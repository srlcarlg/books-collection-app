import 'book.dart';

class Author {
  int? id;
  String? name;
  List<Book>? books;

  Author({
    this.id,
    this.name,
    this.books,
  });

  Author.empty();

  factory Author.fromJson(Map<String, dynamic> json) {
    return Author(
      id: json['id'] != null ? int.parse(json['id'].toString()) : null,
      name: json['name'],
      books: json["books"] != null ? List<Book>.from(json["books"].map((x) => Book.fromJson(x))) : [],
    );
  }

  Map<String, dynamic> toJson(Author author) {
    return {
      "name": author.name,
    };
  }
  Map<String, dynamic> toJsonID(Author author) {
    return {
      "id": author.id,
    };
  }
}
