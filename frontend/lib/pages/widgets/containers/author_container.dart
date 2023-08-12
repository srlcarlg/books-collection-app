import 'package:flutter/material.dart';

import '../../../models/author.dart';

class AuthorContainer extends StatelessWidget {
  final Author author;
  const AuthorContainer({Key? key, required this.author}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const SizedBox(
          height: 210,
          width: 260,
          child: ClipRRect(
            child: Image(image: AssetImage('assets/no_cover.png'))
          ),
        ),
        const SizedBox(height: 10),
        Text(
          author.name!,
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
                    "${author.books?.length ?? 'nº'} books",
                    style: const TextStyle(color: Colors.grey, fontSize: 14),
                  ),
                ),
              ],
            )),
        /*Text(
          author.categories
              .map((e) => e.name)
              .toString()
              .replaceAll("(", "")
              .replaceAll(")", "")
              .replaceAll(",", "\n"),
          style: const TextStyle(color: Colors.black, fontSize: 10),
        ),*/

      ],
    );
  }
}
