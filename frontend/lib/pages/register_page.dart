import 'package:books_collection/models/enums/type_user.dart';
import 'package:books_collection/models/insert_user.dart';
import 'package:books_collection/pages/login_page.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../providers/user_provider.dart';
import 'widgets/custom_widgets.dart';

class RegisterPage extends StatefulWidget {
  const RegisterPage({Key? key}) : super(key: key);
  @override
  State<RegisterPage> createState() => _RegisterPageState();
}

class _RegisterPageState extends State<RegisterPage> {
  TextEditingController nameController = TextEditingController();
  TextEditingController emailController = TextEditingController();
  TextEditingController passwordController = TextEditingController();
  TextEditingController confirmPasswordController = TextEditingController();
  bool _nameFieldValidate = false;
  bool _emailFieldValidate = false;
  bool _passwordFieldValidate = false;
  bool disableFrontValidators = false;
  late SharedPreferences prefs;

  void initSharedPref() async {
    prefs = await SharedPreferences.getInstance();
  }

  void validate(String fieldName) async {
    if (fieldName == "email") {
      if (emailController.text.isNotEmpty && !emailController.text.contains("@")) {
        setState(() {
          _emailFieldValidate = true;
        });
      } else {
        setState(() {
          _emailFieldValidate = false;
        });
      }
    }
    if (fieldName == "password" && _passwordFieldValidate) {
      setState(() {
        _passwordFieldValidate = false;
      });
    }
    if (fieldName == "name" && _nameFieldValidate) {
      setState(() {
        _nameFieldValidate = false;
      });
    }
  }

  @override
  void initState() {
    super.initState();
    initSharedPref();
  }

  @override
  Widget build(BuildContext context) {
    final userProvider = Provider.of<UserProvider>(context, listen: false);
    return SafeArea(
        child: Scaffold(
          body: Container(
            width: MediaQuery.of(context).size.width,
            height: MediaQuery.of(context).size.height,
            decoration: const BoxDecoration(
              gradient: LinearGradient(
                  colors: [Colors.blueAccent, Colors.lightBlueAccent],
                  begin: FractionalOffset.topCenter,
                  end: FractionalOffset.bottomCenter,
                  stops: [0.0, 0.8],
                  tileMode: TileMode.clamp),
            ),
            child: Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  const Text(
                    'Books Collection',
                    style: TextStyle(
                        color: Colors.black,
                        fontSize: 24,
                        fontWeight: FontWeight.bold),
                  ),
                  const Text("Create your account",
                      style: TextStyle(color: Colors.black26, fontSize: 18)),
                  const SizedBox(
                    height: 10,
                  ),
                  SizedBox(
                    width: 400.0,
                    child: TextField(
                      onChanged: (x) => validate("name"),
                      controller: nameController,
                      keyboardType: TextInputType.text,
                      decoration: InputDecoration(
                          filled: true,
                          fillColor: Colors.white,
                          hintText: "Name",
                          errorText: _nameFieldValidate ? "Cannot be empty." : null,
                          border: const OutlineInputBorder(
                            borderRadius: BorderRadius.all(Radius.circular(10.0)),
                          )),
                    ),
                  ),
                  const SizedBox(
                    height: 10,
                  ),
                  SizedBox(
                    width: 400.0,
                    child: TextField(
                      onChanged: (x) => validate("email"),
                      controller: emailController,
                      keyboardType: TextInputType.text,
                      decoration: InputDecoration(
                          filled: true,
                          fillColor: Colors.white,
                          hintText: "Email",
                          errorText: _emailFieldValidate ? "" : null,
                          errorMaxLines: 1,
                          errorStyle: const TextStyle(
                            color: Colors.transparent,
                            fontSize: 0,
                          ),
                          border: const OutlineInputBorder(
                            borderRadius: BorderRadius.all(Radius.circular(10.0)),
                          )),
                    ),
                  ),
                  const SizedBox(
                    height: 10,
                  ),
                  SizedBox(
                    width: 400.0,
                    child: TextField(
                      onChanged: (x) => validate("password"),
                      obscureText: true,
                      controller: passwordController,
                      keyboardType: TextInputType.text,
                      decoration: InputDecoration(
                          filled: true,
                          fillColor: Colors.white,
                          hintText: "Password",
                          errorText:
                          _passwordFieldValidate ? "Cannot be empty." : null,
                          border: const OutlineInputBorder(
                              borderRadius:
                              BorderRadius.all(Radius.circular(10.0)))),
                    ),
                  ),
                  const SizedBox(
                    height: 10,
                  ),
                  SizedBox(
                    width: 400.0,
                    child: TextField(
                      onChanged: (x) => validate("Confirm Password"),
                      obscureText: true,
                      controller: confirmPasswordController,
                      keyboardType: TextInputType.text,
                      decoration: InputDecoration(
                          filled: true,
                          fillColor: Colors.white,
                          hintText: "Confirm password",
                          errorText:
                          _passwordFieldValidate ? "Cannot be empty." : null,
                          border: const OutlineInputBorder(
                              borderRadius:
                              BorderRadius.all(Radius.circular(10.0)))),
                    ),
                  ),
                  const SizedBox(
                    height: 10,
                  ),
                  SizedBox(
                    width: 400,
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        CustomTextButton(
                          messageText: "Already have account?",
                          textButton: "Sign In",
                          onPressed: () {
                            Navigator.push(context, MaterialPageRoute(builder: (context)=>const LoginPage()));
                          },
                        ),
                        ElevatedButton(
                          onPressed: () async {
                            if (nameController.text.isNotEmpty && emailController.text.isNotEmpty &&
                                passwordController.text.isNotEmpty && confirmPasswordController.text.isNotEmpty ||
                                disableFrontValidators) {

                              UserInsert newUser = UserInsert(
                                  name: nameController.text,
                                  email: emailController.text,
                                  password: passwordController.text,
                                  confirmationPassword: confirmPasswordController.text,
                                  typeUser: TypeUser.client
                              );
                              await userProvider.addUser(newUser, context, prefs);
                            }
                            if (!disableFrontValidators) {
                              if (nameController.text.isEmpty) {
                                setState(() {
                                  _nameFieldValidate = true;
                                });
                              }
                              if (emailController.text.isEmpty) {
                                setState(() {
                                  _emailFieldValidate = true;
                                });
                              }
                              if (passwordController.text.isEmpty) {
                                setState(() {
                                  _passwordFieldValidate = true;
                                });
                              }
                            }
                          },
                          child: const Text("REGISTER"),
                        ),
                        CustomSwitch(
                          text: "Disable \n Frontend \n Validators",
                          value: disableFrontValidators,
                          onChanged: (value) {
                            setState(() {
                              disableFrontValidators = value;
                            });
                          },
                        )
                      ],
                    )
                  )
                ],
              ),
            ),
          ),
        ));
  }
}
