import 'package:flutter/material.dart';
import 'package:internet_file/internet_file.dart';
import 'package:pdfx/pdfx.dart';

import '../../models/book.dart';

class BookViewer extends StatelessWidget {
  const BookViewer({super.key, required this.book});
  final Book book;
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: Colors.white,
        appBar: AppBar(
          title:  Text(
            book.title!,
            style: const TextStyle(fontWeight: FontWeight.w700),
          ),
          centerTitle: true,
          actions: const [SizedBox()],
        ),
        body: book.bookUrl! == '' ?
        Padding(
            padding: const EdgeInsets.symmetric(horizontal: 10),
            child: Text(book.description != null ?
          "Book URL unavailable, showing book description instead: \n\n${book.description!}" :
          "Book without description and URL")) :
        PdfViewPinch(
            controller: PdfControllerPinch(
                document: PdfDocument.openData(
                    InternetFile.get(book.bookUrl!))),
        ));
  }
}
