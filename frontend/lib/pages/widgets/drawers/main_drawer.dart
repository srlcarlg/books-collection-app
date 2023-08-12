import 'package:books_collection/models/enums/type_user.dart';
import 'package:books_collection/models/update_user.dart';
import 'package:books_collection/pages/login_page.dart';
import 'package:books_collection/pages/authors_page.dart';
import 'package:books_collection/pages/category_page.dart';
import 'package:books_collection/pages/home_page.dart';
import 'package:books_collection/pages/users_page.dart';
import 'package:books_collection/providers/user_provider.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

class MainDrawer extends StatefulWidget {
  const MainDrawer({Key? key}) : super(key: key);
  @override
  State<MainDrawer> createState() => _MainDrawerState();
}

class _MainDrawerState extends State<MainDrawer> {
  late SharedPreferences prefs;
  String? name;
  String? email;
  TypeUser? typeUser;

  TextEditingController newNameController = TextEditingController();
  TextEditingController newEmailController = TextEditingController();
  TextEditingController oldPasswordController = TextEditingController();
  TextEditingController newPasswordController = TextEditingController();
  TextEditingController confirmController = TextEditingController();

  final _formKey = GlobalKey<FormState>();

  void initSharedPref() async {
    prefs = await SharedPreferences.getInstance();
    setState(() {
      name = prefs.getString("name");
      email = prefs.getString("email");
      typeUser = TypeUser.values.firstWhere((e) => e.name == prefs.getString("typeUser")!.toLowerCase());
    });
  }

  @override
  void initState() {
    super.initState();
    initSharedPref();
  }

  @override
  Widget build(BuildContext context) {
    final userProvider = Provider.of<UserProvider>(context, listen: false);
    return Drawer(
      child: Column(
        children: [
          UserAccountsDrawerHeader(
            currentAccountPicture: CircleAvatar(child: Text(name?[0] ?? '')),
            accountName: Text(name ?? ''),
            accountEmail: Padding(
              padding: const EdgeInsets.only(right: 20),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(email ?? '',
                  style: const TextStyle(
                      color: Colors.grey,
                      //fontSize: 18
                  )),
                  Text(typeUser?.name.toUpperCase() ?? '',
                  style: const TextStyle(
                    color: Colors.amber,
                    //fontSize: 18
                  ))
              ])
            )
          ),
          ElevatedButton(onPressed: () async {
              await userProvider.reAuthenticate(context, prefs);
            },
            child: const Text("reAuthenticate")),
          const SizedBox(height: 5),
          ExpansionTile(
              leading: const Icon(Icons.category),
              title: const Text('Pages'),
              children: [
                ListTile(
                  leading: const Icon(Icons.book),
                  contentPadding: const EdgeInsets.symmetric(horizontal: 30),
                  title: const Text('Books'),
                  onTap: () {
                    if (context.mounted) Navigator.push(context, MaterialPageRoute(builder: (context) => const HomePage()));
                  },
                ),
                typeUser != TypeUser.client ? ListTile(
                  leading: const Icon(Icons.group),
                  contentPadding: const EdgeInsets.symmetric(horizontal: 30),
                  title: const Text('Authors'),
                  onTap: () {
                    if (context.mounted) Navigator.push(context, MaterialPageRoute(builder: (context) => const AuthorPage()));
                  },
                ) : const SizedBox(),
                typeUser != TypeUser.client ? ListTile(
                  leading: const Icon(Icons.list),
                  contentPadding: const EdgeInsets.symmetric(horizontal: 30),
                  title: const Text('Categories'),
                  onTap: () {
                    if (context.mounted) Navigator.push(context, MaterialPageRoute(builder: (context) => const CategoryPage()));
                  },
                ) : const SizedBox(),
                typeUser == TypeUser.admin ? ListTile(
                  leading: const Icon(Icons.person),
                  contentPadding: const EdgeInsets.symmetric(horizontal: 30),
                  title: const Text('Users'),
                  onTap: () {
                    if (context.mounted) Navigator.push(context, MaterialPageRoute(builder: (context) => const UserPage()));
                  },
                ) : const SizedBox(),
              ]
          ),
          ExpansionTile(
            leading: const Icon(Icons.account_box),
            title: const Text('Account'),
            children: [
              ListTile(
                contentPadding: const EdgeInsets.symmetric(horizontal: 30),
                title: const Text('Change Name/Email'),
                onTap: () {
                  showDialog(
                      context: context,
                      builder: (BuildContext context) {
                        return AlertDialog(
                          scrollable: true,
                          title: const Text('New name/email'),
                          content: Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Form(
                              child: Column(
                                children: <Widget>[
                                  TextFormField(
                                    controller: newNameController,
                                    decoration: const InputDecoration(
                                      labelText: 'New Name',
                                      icon: Icon(Icons.account_box),
                                    ),
                                  ),
                                  TextFormField(
                                    controller: newEmailController,
                                    decoration: const InputDecoration(
                                      labelText: 'New Email',
                                      icon: Icon(Icons.email),
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          ),
                          actions: [
                            ElevatedButton(
                                child: const Text("Submit"),
                                onPressed: () async {
                                  UserUpdate update = UserUpdate(
                                      name: newNameController.text,
                                      email: newEmailController.text);
                                  await userProvider.updateAuthenticatedUser(update, context, prefs);
                                })
                          ],
                        );
                      });
                },
              ),
              ListTile(
                contentPadding: const EdgeInsets.symmetric(horizontal: 30),
                title: const Text('Change Password'),
                onTap: () {
                  showDialog(
                      context: context,
                      builder: (BuildContext context) {
                        return AlertDialog(
                          scrollable: true,
                          title: const Text('New password'),
                          content: Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Form(
                              key: _formKey,
                              child: Column(
                                children: <Widget>[
                                  TextFormField(
                                    obscureText: true,
                                    controller: oldPasswordController,
                                    decoration: const InputDecoration(
                                      labelText: 'Old Password',
                                    ),
                                    validator: (value) {
                                      if (value == null || value.isEmpty) {
                                        return 'Cannot be empty.';
                                      }
                                      return null;
                                    },
                                  ),
                                  TextFormField(
                                    controller: newPasswordController,
                                    obscureText: true,
                                    decoration: const InputDecoration(
                                      labelText: 'New Password',

                                    ),
                                    validator: (value) {
                                      if (value == null || value.isEmpty) {
                                        return 'Cannot be empty.';
                                      }
                                      return null;
                                    },
                                  ),
                                  TextFormField(
                                    controller: confirmController,
                                    obscureText: true,
                                    decoration: const InputDecoration(
                                      labelText: 'Confirm',
                                    ),
                                    validator: (value) {
                                      if (value == null || value.isEmpty) {
                                        return 'Cannot be empty.';
                                      }
                                      return null;
                                    },
                                  ),
                                ],
                              ),
                            ),
                          ),
                          actions: [
                            ElevatedButton(
                                child: const Text("Submit"),
                                onPressed: () async {
                                  if (!_formKey.currentState!.validate()) return;
                                  UserUpdate update = UserUpdate(
                                      password: oldPasswordController.text,
                                      newPassword: newPasswordController.text,
                                      confirmationPassword: confirmController.text);
                                  await userProvider.updateAuthenticatedUser(update, context, prefs);
                                })
                          ],
                        );
                      });
                },
              ),
            ],
          ),
          ListTile(leading: const Icon(Icons.arrow_back),
              title: const Text("Logout"),
              onTap: () async {
                await userProvider.logout(context, prefs);
                if (context.mounted) Navigator.push(context, MaterialPageRoute(builder: (context) => const LoginPage()));
              }),
        ],
      ),
    );
  }
}
