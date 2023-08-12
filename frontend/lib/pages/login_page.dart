import 'package:books_collection/config.dart';
import 'package:books_collection/pages/register_page.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../providers/user_provider.dart';
import 'widgets/custom_widgets.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({Key? key}) : super(key: key);
  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  TextEditingController emailController = TextEditingController();
  TextEditingController passwordController = TextEditingController();
  bool _emailFieldValidate = false;
  bool _passwordFieldValidate = false;
  late SharedPreferences prefs;

  void initSharedPref() async {
    prefs = await SharedPreferences.getInstance();
  }

  void validate(String fieldName) async {
    if (fieldName == "email") {
      if (emailController.text.isNotEmpty &
          !emailController.text.contains("@")) {
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
  }

  void url(String url) async {
    setState(() {
      serverUrl = url;
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
              const Text("Sign in to continue",
                  style: TextStyle(color: Colors.black26, fontSize: 18)),
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
                  width: 400,
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      const SizedBox(
                        width: 100,
                      ),
                      ElevatedButton(
                        onPressed: () async {
                          if (emailController.text.isNotEmpty && passwordController.text.isNotEmpty) {
                             await userProvider.login(
                                emailController.text, passwordController.text, context, prefs
                             );
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
                        },
                        child: const Text("LOGIN"),
                      ),
                      CustomTextButton(
                        messageText: "Don't have an account?",
                        textButton: "Sign Up",
                        onPressed: () {
                          Navigator.push(
                              context,
                              MaterialPageRoute(
                                  builder: (context) => const RegisterPage()));
                        },
                      ),
                    ],
                  )),
              const SizedBox(height: 10,),
              SizedBox(
                width: 400.0,
                child: TextFormField(
                  onChanged: (x) => url(x),
                  initialValue: serverUrl,
                  decoration: const InputDecoration(
                    labelText: 'API URL',
                    icon: Icon(Icons.computer),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    ));
  }
}
