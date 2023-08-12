import 'package:books_collection/config.dart';
import 'package:books_collection/models/book.dart';
import 'package:books_collection/models/enums/book_status.dart';
import 'package:flutter/material.dart';

class BookContainer extends StatelessWidget {
  final Book book;
  const BookContainer({Key? key, required this.book}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        SizedBox(
          height: 210,
          width: 260,
          child: ClipRRect(
            child: book.coverUrl == '' ?
            const Image(image: AssetImage('assets/no_cover.png')) :
            Image.network(
              book.coverUrl!.contains(serverUrl) ?
              book.coverUrl! :
              "http://$serverUrl${book.coverUrl!}",
              fit: BoxFit.scaleDown,
              //cacheHeight: 300,
              //cacheWidth: 300,
            ),
          ),
        ),
        const SizedBox(height: 10),
        Text(
          book.title!,
          style: const TextStyle(
            color: Colors.black,
            fontSize: 15,
            fontWeight: FontWeight.w700,
          ),
        ),
        Padding(
            padding: const EdgeInsets.only(right: 10),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Expanded(
                  child: Text(
                    book.authors
                        .map((e) => e.name)
                        .toString()
                        .replaceAll("(", "")
                        .replaceAll(")", ""),
                    style: const TextStyle(color: Colors.grey, fontSize: 14),
                  ),
                ),
                Text(
                  BookStatus.values
                      .firstWhere((e) => e == book.bookStatus)
                      .name
                      .toUpperCase(),
                  style: const TextStyle(
                    color: Colors.black,
                  ),
                ),
              ],
            )),
        Text(
          book.categories
              .map((e) => e.name)
              .toString()
              .replaceAll("(", "")
              .replaceAll(")", "")
              .replaceAll(",", "\n"),
          style: const TextStyle(color: Colors.black, fontSize: 10),
        ),
      ],
    );
  }
}
