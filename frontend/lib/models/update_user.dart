
class UserUpdate {
  int? id;
  String? name;
  String? email;
  String? password;
  String? newPassword;
  String? confirmationPassword;

  UserUpdate({
    this.id,
    this.name,
    this.email,
    this.password,
    this.newPassword,
    this.confirmationPassword,
  });

  UserUpdate.empty();

  Map<String, dynamic> toJson(UserUpdate userUpdate) {
    Map<String, dynamic> rawJson = {
      "id": userUpdate.id,
      "name": userUpdate.name,
      "email": userUpdate.email,
      "password": userUpdate.password,
      "new_password": userUpdate.newPassword,
      "confirmation_password": userUpdate.confirmationPassword,
    };
    rawJson.removeWhere((key, value) => value == "" || value == null);
    return rawJson;
  }
}
