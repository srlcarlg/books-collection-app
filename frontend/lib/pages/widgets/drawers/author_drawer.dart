import 'package:books_collection/models/author.dart';
import 'package:books_collection/providers/author_provider.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../custom_widgets.dart';

class AuthorDrawer extends StatefulWidget {
  const AuthorDrawer({Key? key}) : super(key: key);

  @override
  State<AuthorDrawer> createState() => _AuthorDrawerState();
}

class _AuthorDrawerState extends State<AuthorDrawer> {
  late final Author _newAuthor = Author.empty();
  Author? _editAuthor;
  TextEditingController nameController = TextEditingController();

  final _formKey = GlobalKey<FormState>();
  late SharedPreferences prefs;

  void initSharedPref() async {
    prefs = await SharedPreferences.getInstance();
  }

  @override
  void initState() {
    super.initState();
    initSharedPref();
    Future.delayed(Duration.zero, () {
      _editAuthor = Provider.of<AuthorProvider>(context, listen: false).authorToEdit;
      if (_editAuthor != null) {
        setState(() {
          nameController = TextEditingController(text: _editAuthor!.name);
        });
      }
    });

  }

  @override
  Widget build(BuildContext context) {
    final authorProvider = Provider.of<AuthorProvider>(context, listen: false);
    return Drawer(
      width: 500,
      child: Form(
        key: _formKey,
        child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 20),
        child: ListView(
          children: [
            const SizedBox(height: 40),
            Text(
              _editAuthor == null ? 'Add Author' : 'Edit Author',
              style: const TextStyle(
                color: Colors.black,
                fontWeight: FontWeight.bold,
                fontSize: 20,
              ),
            ),
            const SizedBox(height: 20),
            TextFormField(
              controller: nameController,
              decoration: const InputDecoration(labelText: "Name*"),
              validator: (value) {
                if (value == null || value.isEmpty) {
                  return 'Cannot be empty.';
                }
                return null;
              },
              onChanged: (value) {
                if (_editAuthor == null) {
                  _newAuthor.name = value;
                } else {
                  _editAuthor!.name = value;
                }
              },
            ),
            const SizedBox(height: 40),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Expanded(
                  child: CustomElevatedButton(
                    text: _editAuthor == null ? 'Add Author' : 'Edit',
                    icon: _editAuthor == null ? Icons.add : Icons.edit,
                    onPressed: () {
                      if (!_formKey.currentState!.validate()) return;
                      if (_editAuthor == null) {
                        authorProvider.addAuthor(
                            _newAuthor, context, prefs
                        );
                      } else {
                        authorProvider.editAuthor(
                            _editAuthor!, context, prefs
                        );
                      }
                    },
                  ),
                ),
                _editAuthor != null
                    ? Padding(
                        padding: const EdgeInsets.only(left: 5),
                        child: CustomElevatedButton(
                          text: 'Delete',
                          icon: Icons.delete,
                          color: Colors.red,
                          onPressed: () async {
                            await authorProvider.deleteAuthor(_editAuthor!, context, prefs);
                          },
                        ),
                      )
                    : const SizedBox(),
              ],
            ),
            const SizedBox(height: 20),
          ],
        ),
      ),
    ));
  }

}
