import 'package:books_collection/pages/widgets/book_viewer.dart';
import 'package:books_collection/pages/widgets/custom_widgets.dart';
import 'package:books_collection/pages/widgets/drawers/book_drawer.dart';
import 'package:books_collection/pages/widgets/containers/book_container.dart';
import 'package:books_collection/pages/widgets/drawers/main_drawer.dart';
import 'package:books_collection/providers/favorites_provider.dart';
import 'package:dropdown_search/dropdown_search.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:responsive_grid_list/responsive_grid_list.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../models/enums/type_user.dart';
import '../providers/author_provider.dart';
import '../providers/book_provider.dart';
import '../providers/category_provider.dart';
import 'authors_page.dart';

class HomePage extends StatefulWidget {
  const HomePage({Key? key}) : super(key: key);
  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  final GlobalKey<ScaffoldState> _scaffoldKey = GlobalKey<ScaffoldState>();
  List<String> filterBy = ['Author', 'Category', 'Favorites', "None"];
  List<String> pageSizes = ['5', '10', '25', '50', '100'];
  List<String> dynamicBy = [];
  String? filterValue;
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
      await Provider.of<BookProvider>(context, listen: false)
          .getBooks(0, pageSizeValue, "", context, prefs);
      if (context.mounted) {
        pagesNum = Provider.of<BookProvider>(context, listen: false).pagesNumber;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    final bookProvider = Provider.of<BookProvider>(context, listen: false);
    final authorProvider = Provider.of<AuthorProvider>(context, listen: false);
    final categoryProvider = Provider.of<CategoryProvider>(context, listen: false);
    final favoriteProvider = Provider.of<FavoriteProvider>(context, listen: false);

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
      endDrawer: const AddBookDrawer(),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const SizedBox(height: 10),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 30),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Row(
                  children: [
                    const Text(
                      'Books',
                      style: TextStyle(fontSize: 25, fontWeight: FontWeight.bold),
                    ),
                    typeUser != TypeUser.client ?
                    IconButton(
                      onPressed: () {
                        if (context.mounted) Navigator.push(context, MaterialPageRoute(builder: (context) => const AuthorPage()));
                      },
                      icon: const Icon(Icons.navigate_next)
                    ) : const SizedBox(),
                  ],
                ),
                CustomElevatedButton(
                  text: 'Add Book',
                  icon: Icons.add,
                  onPressed: () {
                    bookProvider
                        .bookToEdit = null;
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
                Expanded(
                  //width: 180,
                  child: TextField(
                    decoration: const InputDecoration(
                      contentPadding: EdgeInsets.symmetric(vertical: 15),
                      hintText: 'Search by title...',
                      border: InputBorder.none,
                      prefixIcon: Icon(Icons.search),
                    ),
                    onChanged: (input) async {
                      bookProvider
                          .getBooks(0, 999, "", context, prefs, title: input);
                      if (dynamicBy.isNotEmpty) {
                        setState(() {
                          pagesNum = 0;
                          filterValue = null;
                          dynamicBy = [];
                          nextPage = 1;
                        });
                      }
                    },
                  ),
                ),
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
                      await bookProvider
                          .getBooks(0, pageSizeValue, "", context, prefs);
                      setState(() {
                        pagesNum = bookProvider.pagesNumber;
                        nextPage = 1;
                      });
                    },
                  ),
                ),
                const SizedBox(width: 10),
                SizedBox(
                  //width: 100,
                  child: DropdownButton(
                    hint: const Text("Filter By"),
                    value: filterValue,
                    items: filterBy.map((String value) {
                      return DropdownMenuItem<String>(
                        value: value,
                        child: Text(value),
                      );
                    }).toList(),
                    onChanged: (value) async {
                      switch (value) {
                        case "Author": {
                          await authorProvider
                              .getAuthors(0, pageSizeValue, "", context, prefs);
                          setState(() {
                            filterValue = value;
                            dynamicBy = authorProvider.authors.map((e) => e.name!).toList();
                          });
                        } break;
                        case "Category": {
                          await categoryProvider
                              .getCategories(0, pageSizeValue, "", context, prefs);
                          setState(() {
                            filterValue = value;
                            dynamicBy = categoryProvider.categories.map((e) => e.name!).toList();
                          });
                        } break;
                        case "Favorites": {
                          await favoriteProvider
                              .getFavoriteBooks(0, pageSizeValue, "", context, prefs);
                          bookProvider.books = favoriteProvider.favoriteBooks;
                          setState(() {
                            pagesNum = favoriteProvider.pagesNumber;
                            nextPage = 1;

                            filterValue = value;
                            dynamicBy = [];
                          });
                        } break;
                        case "None": {
                          setState(() {
                            filterValue = null;
                            dynamicBy = [];
                          });
                        } break;
                      }
                    },
                  ),
                ),
                const SizedBox(width: 10),
                filterValue != null && filterValue != "Favorites" ?
                SizedBox(
                  width: 150,
                  child: DropdownSearch<String>(
                    popupProps: const PopupProps.menu(
                      showSearchBox: true,
                      showSelectedItems: true,
                    ),
                    dropdownDecoratorProps: DropDownDecoratorProps(
                      dropdownSearchDecoration: InputDecoration(
                        labelText: "Select $filterValue",
                      ),
                    ),
                    items: dynamicBy,
                    onChanged: (value) async {
                      switch (filterValue) {
                        case "Author": {
                          await authorProvider
                              .getAuthorInfo(
                              authorProvider.authors.firstWhere((e) => e.name == value!),
                              context, prefs);
                          bookProvider.books = authorProvider.authorBooks;
                          setState(() {
                            pagesNum = 0;
                            nextPage = 1;
                          });
                        } break;
                        case "Category": {
                          await categoryProvider
                              .getCategoryInfo(
                              categoryProvider.categories.firstWhere((e) => e.name == value!),
                              context, prefs);
                          bookProvider.books = categoryProvider.categoryBooks;
                          setState(() {
                            pagesNum = 0;
                            nextPage = 1;
                          });
                        } break;
                        case "Favorites": {
                          await favoriteProvider
                              .getFavoriteBooks(0, pageSizeValue, "", context, prefs);
                          bookProvider.books = favoriteProvider.favoriteBooks;
                          setState(() {
                            pagesNum = favoriteProvider.pagesNumber;
                            nextPage = 1;
                          });
                        } break;
                      }
                    },
                  ),
                )
                : const SizedBox(width: 10),
                const SizedBox(width: 10),
                CustomElevatedButton(
                  text: "Refresh",
                  icon: Icons.refresh ,
                  color: Colors.white,
                  onPressed: () async {
                    await bookProvider
                        .getBooks(0, pageSizeValue, "", context, prefs);
                    setState(() {
                      pagesNum = bookProvider.pagesNumber;
                      filterValue = null;
                      dynamicBy = [];
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
              Provider.of<BookProvider>(context).books.map<Widget>((book) {
                return GestureDetector(
                  onTap: () {
                    if (typeUser == TypeUser.client) {
                      if (context.mounted) Navigator.push(context, MaterialPageRoute(builder: (context) => BookViewer(book: book)));
                    }
                    else {
                      bookProvider
                        .bookToEdit = book;
                      _scaffoldKey.currentState!.openEndDrawer();
                    }
                  },
                  child: BookContainer(book: book),
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
                                      bookProvider.getBooks(nextPage, pageSizeValue, "PAGING", context, prefs);
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
