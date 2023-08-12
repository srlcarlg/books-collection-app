import 'package:books_collection/models/insert_user.dart';
import 'package:books_collection/providers/user_provider.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../../models/enums/type_user.dart';
import '../custom_widgets.dart';

class UserDrawer extends StatefulWidget {
  const UserDrawer({Key? key}) : super(key: key);

  @override
  State<UserDrawer> createState() => _UserDrawerState();
}

class _UserDrawerState extends State<UserDrawer> {
  late final UserInsert _newUser = UserInsert.empty();
  UserInsert? _editUser;
  TextEditingController nameController = TextEditingController();
  TextEditingController emailController = TextEditingController();
  TypeUser typeUser = TypeUser.client;
  TextEditingController passwordController = TextEditingController();
  TextEditingController confirmController = TextEditingController();

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
      _editUser = Provider.of<UserProvider>(context, listen: false).userToEdit;
      if (_editUser != null) {
        setState(() {
          nameController = TextEditingController(text: _editUser!.name);
          emailController = TextEditingController(text: _editUser!.email);
          typeUser = _editUser!.typeUser!;
        });
      }
    });

  }

  @override
  Widget build(BuildContext context) {
    final userProvider = Provider.of<UserProvider>(context, listen: false);
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
              _editUser == null ? 'Add User' : 'Edit User',
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
                if (_editUser == null) {
                  _newUser.name = value;
                } else {
                  _editUser!.name = value;
                }
              },
            ),
            const SizedBox(height: 20),
            TextFormField(
              controller: emailController,
              decoration: const InputDecoration(labelText: 'Email*'),
              validator: (value) {
                if (value == null || value.isEmpty) {
                  return 'Cannot be empty.';
                }
                return null;
              },
              onChanged: (value) {
                if (_editUser == null) {
                  _newUser.email = value;
                } else {
                  _editUser!.email = value;
                }
              },
            ),
            const SizedBox(height: 20),
            SizedBox(
              child: DropdownButton(
                hint: const Text("Type User*"),
                value: typeUser.name.toUpperCase(),
                items: TypeUser.values.map((var value) {
                  return DropdownMenuItem<String>(
                    value: value.name.toUpperCase(),
                    child: Text(value.name.toUpperCase()),
                  );
                }).toList(),
                onChanged: (value) {
                  if (_editUser == null) {
                    _newUser.
                    typeUser = TypeUser.values.firstWhere((e) => e.name == value?.toLowerCase());
                  } else {
                    _editUser!.
                    typeUser = TypeUser.values.firstWhere((e) => e.name == value?.toLowerCase());
                  }
                },
              ),
            ),
            const SizedBox(height: 40),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Expanded(
                  child: CustomElevatedButton(
                    text: _editUser == null ? 'Add User' : 'Edit',
                    icon: _editUser == null ? Icons.add : Icons.edit,
                    onPressed: () {
                      if (!_formKey.currentState!.validate()) return;
                      if (_editUser == null) {
                        userProvider.addUser(
                            _newUser, context, prefs
                        );
                      } else {
                        userProvider.updateUserById(
                            _editUser!, context, prefs
                        );
                      }
                    },
                  ),
                ),
                _editUser != null
                    ? Padding(
                        padding: const EdgeInsets.only(left: 5),
                        child: CustomElevatedButton(
                          text: 'Delete',
                          icon: Icons.delete,
                          color: Colors.red,
                          onPressed: () async {
                            await userProvider.deleteUser(_editUser!, context, prefs);
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
