import 'package:books_collection/pages/users_page.dart';
import 'package:books_collection/pages/widgets/containers/category_container.dart';
import 'package:books_collection/pages/widgets/custom_widgets.dart';
import 'package:books_collection/pages/widgets/drawers/category_drawer.dart';
import 'package:books_collection/pages/widgets/drawers/main_drawer.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:responsive_grid_list/responsive_grid_list.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../models/enums/type_user.dart';
import '../providers/category_provider.dart';
import 'authors_page.dart';

class CategoryPage extends StatefulWidget {
  const CategoryPage({Key? key}) : super(key: key);
  @override
  State<CategoryPage> createState() => _CategoryPageState();
}

class _CategoryPageState extends State<CategoryPage> {
  final GlobalKey<ScaffoldState> _scaffoldKey = GlobalKey<ScaffoldState>();
  List<String> pageSizes = ['5', '10', '25', '50', '100'];
  int pagesNum = 0;
  int nextPage = 1;
  int pageSizeValue = 25;


  TypeUser? typeUser;
  late SharedPreferences prefs;

  void initSharedPref() async {
    prefs = await SharedPreferences.getInstance();
    setState(() {
      typeUser = TypeUser.values.firstWhere((e) => e.name == prefs.getString("typeUser")!.toLowerCase());
    });
  }

  @override
  void initState() {
    super.initState();
    initSharedPref();
    Future.delayed(Duration.zero, () async {
      await Provider.of<CategoryProvider>(context, listen: false)
          .getCategories(0, pageSizeValue, "", context, prefs);
      if (context.mounted) {
        pagesNum = Provider.of<CategoryProvider>(context, listen: false).pagesNumber;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    final categoryProvider = Provider.of<CategoryProvider>(context, listen: false);
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
      endDrawer: const CategoryDrawer(),
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
                    typeUser != TypeUser.client ?
                    IconButton(onPressed: () {
                      if (context.mounted) Navigator.push(context, MaterialPageRoute(builder: (context) => const AuthorPage()));
                      },
                      icon: const Icon(Icons.navigate_before)
                    ) : const SizedBox(),
                    const Text(
                      'Categories',
                      style: TextStyle(fontSize: 25, fontWeight: FontWeight.bold),
                    ),
                    typeUser ==TypeUser.admin ?
                    IconButton(onPressed: () {
                        if (context.mounted) Navigator.push(context, MaterialPageRoute(builder: (context) => const UserPage()));
                      },
                      icon: const Icon(Icons.navigate_next)
                    ) : const SizedBox(),
                  ],
                ),
                CustomElevatedButton(
                  text: 'Add Category',
                  icon: Icons.add,
                  onPressed: () {
                    categoryProvider
                        .categoryToEdit = null;
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
                      await categoryProvider
                          .getCategories(0, pageSizeValue, "", context, prefs);
                      setState(() {
                        pagesNum = categoryProvider.pagesNumber;
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
                    await categoryProvider
                        .getCategories(0, pageSizeValue, "", context, prefs);
                    setState(() {
                      pagesNum = categoryProvider.pagesNumber;
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
              Provider.of<CategoryProvider>(context).categories.map<Widget>((category) {
                return GestureDetector(
                  onTap: () {
                    categoryProvider
                        .categoryToEdit = category;
                    _scaffoldKey.currentState!.openEndDrawer();
                  },
                  child: CategoryContainer(category: category),
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
                                      categoryProvider.getCategories(nextPage, pageSizeValue, "PAGING", context, prefs);
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
