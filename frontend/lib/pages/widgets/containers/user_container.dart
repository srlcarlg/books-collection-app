import 'package:flutter/material.dart';

import '../../../models/enums/type_user.dart';
import '../../../models/insert_user.dart';

class UserContainer extends StatelessWidget {
  final UserInsert user;
  const UserContainer({Key? key, required this.user}) : super(key: key);
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
          user.name!,
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
                    user.email!,
                    style: const TextStyle(color: Colors.grey, fontSize: 14),
                  ),
                ),
                /*Text(
                  BookStatus.values
                      .firstWhere((e) => e == book.bookStatus)
                      .name
                      .toUpperCase(),
                  style: const TextStyle(
                    color: Colors.black,
                  ),
                ),*/
              ],
            )),
        Text(
          TypeUser.values.firstWhere((e) => e == user.typeUser!).name.toUpperCase(),
          style: const TextStyle(color: Colors.black, fontSize: 12),
        ),
      ],
    );
  }
}
