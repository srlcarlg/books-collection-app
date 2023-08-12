import 'book.dart';

class Category {
  int? id;
  String? name;
  List<Book>? books;

  Category({
    this.id,
    this.name,
    this.books,
  });

  Category.empty();

  factory Category.fromJson(Map<String, dynamic> json) {
    return Category(
        id: json['id'] != null ? int.parse(json['id'].toString()) : null,
        name: json['name'],
        books: json["books"] != null ? List<Book>.from(json["books"].map((x) => Book.fromJson(x))) : [],
    );
  }

  Map<String, dynamic> toJson(Category category) {
    return {
      "name": category.name,
    };
  }
  Map<String, dynamic> toJsonID(Category category) {
    return {
      "id": category.id,
    };
  }
}
