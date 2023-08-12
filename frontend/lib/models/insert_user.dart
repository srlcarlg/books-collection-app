import 'package:books_collection/models/enums/type_user.dart';

class UserInsert {

  int? id;
  String? name;
  String? email;
  String? password;
  String? confirmationPassword;
  TypeUser? typeUser = TypeUser.client;

  UserInsert({
    this.id,
    this.name,
    this.email,
    this.password,
    this.confirmationPassword,
    this.typeUser = TypeUser.client
  });

  UserInsert.empty();

  Map<String, dynamic> toJson(UserInsert userUpdate) {
    Map<String, dynamic> rawJson = {
      "name": userUpdate.name,
      "email": userUpdate.email,
      "password": userUpdate.password,
      "confirmation_password": userUpdate.confirmationPassword,
      "type_user": TypeUser.values.firstWhere((e) => e == userUpdate.typeUser!).name.toUpperCase()
    };
    rawJson.removeWhere((key, value) => value == "" || value == null);
    return rawJson;
  }

  factory UserInsert.fromJson(Map<String, dynamic> json) {
    return UserInsert(
      id: json['id'],
      name: json['name'],
      email: json['email'],
      typeUser: TypeUser.values.firstWhere((e) => e.name == json['type_user'].toLowerCase()),
    );
  }
}
