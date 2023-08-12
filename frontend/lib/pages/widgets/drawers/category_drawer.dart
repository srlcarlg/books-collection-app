import 'package:books_collection/models/category.dart';
import 'package:books_collection/providers/category_provider.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../custom_widgets.dart';

class CategoryDrawer extends StatefulWidget {
  const CategoryDrawer({Key? key}) : super(key: key);

  @override
  State<CategoryDrawer> createState() => _CategoryDrawerState();
}

class _CategoryDrawerState extends State<CategoryDrawer> {
  late final Category _newCategory = Category.empty();
  Category? _editCategory;
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
      _editCategory = Provider.of<CategoryProvider>(context, listen: false).categoryToEdit;
      if (_editCategory != null) {
        setState(() {
          nameController = TextEditingController(text: _editCategory!.name);
        });
      }
    });

  }

  @override
  Widget build(BuildContext context) {
    final authorProvider = Provider.of<CategoryProvider>(context, listen: false);
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
              _editCategory == null ? 'Add Category' : 'Edit Category',
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
                if (_editCategory == null) {
                  _newCategory.name = value;
                } else {
                  _editCategory!.name = value;
                }
              },
            ),
            const SizedBox(height: 40),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Expanded(
                  child: CustomElevatedButton(
                    text: _editCategory == null ? 'Add Category' : 'Edit',
                    icon: _editCategory == null ? Icons.add : Icons.edit,
                    onPressed: () {
                      if (!_formKey.currentState!.validate()) return;
                      if (_editCategory == null) {
                        authorProvider.addCategory(
                            _newCategory, context, prefs
                        );
                      } else {
                        authorProvider.editCategory(
                            _editCategory!, context, prefs
                        );
                      }
                    },
                  ),
                ),
                _editCategory != null
                    ? Padding(
                        padding: const EdgeInsets.only(left: 5),
                        child: CustomElevatedButton(
                          text: 'Delete',
                          icon: Icons.delete,
                          color: Colors.red,
                          onPressed: () async {
                            await authorProvider.deleteCategory(_editCategory!, context, prefs);
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
