import 'package:books_collection/pages/category_page.dart';
import 'package:books_collection/pages/widgets/containers/user_container.dart';
import 'package:books_collection/pages/widgets/custom_widgets.dart';
import 'package:books_collection/pages/widgets/drawers/user_drawer.dart';
import 'package:books_collection/pages/widgets/drawers/main_drawer.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:responsive_grid_list/responsive_grid_list.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../providers/user_provider.dart';

class UserPage extends StatefulWidget {
  const UserPage({Key? key}) : super(key: key);
  @override
  State<UserPage> createState() => _UserPageState();
}

class _UserPageState extends State<UserPage> {
  final GlobalKey<ScaffoldState> _scaffoldKey = GlobalKey<ScaffoldState>();
  List<String> pageSizes = ['5', '10', '25', '50', '100'];
  int pagesNum = 0;
  int nextPage = 1;
  int pageSizeValue = 25;

  late SharedPreferences prefs;

  void initSharedPref() async {
    prefs = await SharedPreferences.getInstance();
  }

  @override
  void initState() {
    super.initState();
    initSharedPref();
    Future.delayed(Duration.zero, () async {
      await Provider.of<UserProvider>(context, listen: false)
          .getUsers(0, pageSizeValue, "", context, prefs);
      if (context.mounted) {
        pagesNum = Provider.of<UserProvider>(context, listen: false).pagesNumber;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    final userProvider = Provider.of<UserProvider>(context, listen: false);
    return Scaffold(
      backgroundColor: Colors.white,
      key: _scaffoldKey,
      appBar: AppBar(
        title: const Text(
          "Books Collection",
          style: TextStyle(fontWeight: FontWeight.w700),
        ),
        centerTitle: true,
        actions: const [SizedBox()],
      ),
      drawer: const MainDrawer(),
      endDrawer: const UserDrawer(),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const SizedBox(height: 10),
          Padding(
            padding: const EdgeInsets.only(right: 30),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Row(
                  children: [
                    IconButton(
                      onPressed: () {
                        if (context.mounted) Navigator.push(context, MaterialPageRoute(builder: (context) => const CategoryPage()));
                      },
                      icon: const Icon(Icons.navigate_before)),
                    const Text(
                      'Users',
                      style: TextStyle(fontSize: 25, fontWeight: FontWeight.bold),
                    ),
                  ],
                ),
                CustomElevatedButton(
                  text: 'Add User',
                  icon: Icons.add,
                  onPressed: () {
                    userProvider
                        .userToEdit = null;
                    _scaffoldKey.currentState!.openEndDrawer();
                  },
                ),
              ],
            ),
          ),
          const SizedBox(height: 20),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 20),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                SizedBox(
                  //width: 50,
                  child: DropdownButton(
                    hint: const Text(""),
                    value: pageSizeValue.toString(),
                    items: pageSizes.map((String value) {
                      return DropdownMenuItem<String>(
                        value: value,
                        child: Text(value),
                      );
                    }).toList(),
                    onChanged: (value) async {
                      pageSizeValue = int.parse(value.toString());
                      await userProvider
                          .getUsers(0, pageSizeValue, "", context, prefs);
                      setState(() {
                        pagesNum = userProvider.pagesNumber;
                        nextPage = 1;
                      });
                    },
                  ),
                ),
                const SizedBox(width: 10),
                CustomElevatedButton(
                  text: "Refresh",
                  icon: Icons.refresh ,
                  color: Colors.white,
                  onPressed: () async {
                    await userProvider
                        .getUsers(0, pageSizeValue, "", context, prefs);
                    setState(() {
                      pagesNum = userProvider.pagesNumber;
                      nextPage = 1;
                    });
                  },
                )
              ],
            ),
          ),
          const SizedBox(height: 30),
          Expanded(
            child: ResponsiveGridList(
              minItemWidth: 170,
              horizontalGridMargin: 20,
              children:
              Provider.of<UserProvider>(context).users.map<Widget>((user) {
                return GestureDetector(
                  onTap: () {
                    userProvider
                        .userToEdit = user;
                    _scaffoldKey.currentState!.openEndDrawer();
                  },
                  child: UserContainer(user: user),
                );
              }).toList()
                    ..add(
                      pagesNum > 1
                          ? Column(
                              mainAxisAlignment: MainAxisAlignment.center,
                              verticalDirection: VerticalDirection.up,
                              children: [
                                SizedBox(
                                  child: CustomElevatedButton(
                                    text: 'Load More',
                                    icon: Icons.add,
                                    color: Colors.grey,
                                    onPressed: () {
                                      userProvider.getUsers(nextPage, pageSizeValue, "PAGING", context, prefs);
                                      pagesNum--;
                                      nextPage++;
                                    },
                                  ),
                                ),
                              ],
                            )
                          : const SizedBox(),
                    ),
            ),
          ),
        ],
      ),
    );
  }
}
