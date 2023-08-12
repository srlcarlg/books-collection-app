import 'package:books_collection/models/author.dart';
import 'package:books_collection/models/category.dart';
import 'package:books_collection/models/enums/book_status.dart';
import 'package:books_collection/providers/author_provider.dart';
import 'package:books_collection/providers/category_provider.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:multi_select_flutter/multi_select_flutter.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../../models/book.dart';
import '../../../providers/book_provider.dart';
import '../book_viewer.dart';
import '../custom_widgets.dart';

class AddBookDrawer extends StatefulWidget {
  const AddBookDrawer({Key? key}) : super(key: key);

  @override
  State<AddBookDrawer> createState() => _AddBookDrawerState();
}

class _AddBookDrawerState extends State<AddBookDrawer> {
  late final Book _newBook = Book.empty();
  Book? _editBook;
  Book? _oldEditBook;
  TextEditingController titleController = TextEditingController();
  TextEditingController publicationYearController = TextEditingController();
  BookStatus bookStatus = BookStatus.missing;
  TextEditingController descriptionController = TextEditingController();
  TextEditingController coverUrlController = TextEditingController();
  TextEditingController bookUrlController = TextEditingController();

  List<bool> isSelected = [true, false];
  String _filename = "";
  FilePickerResult? _filePickerResult;

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

      _editBook = Provider.of<BookProvider>(context, listen: false).bookToEdit;
      if (_editBook != null) {
        setState(() {
          titleController = TextEditingController(text: _editBook!.title);
          publicationYearController = TextEditingController(text: "${_editBook!.publicationYear ?? ''}");
          descriptionController = TextEditingController(text: _editBook!.description);
          coverUrlController = TextEditingController(text: _editBook!.coverUrl);
          bookUrlController = TextEditingController(text: _editBook!.bookUrl);
          _oldEditBook = _editBook!.copy();
        });
      }

      Provider.of<AuthorProvider>(context, listen: false).getAuthors(
          0, 100000, "", context, prefs);

      Provider.of<CategoryProvider>(context, listen: false).getCategories(
          0, 100000, "", context, prefs);
    });

  }

  @override
  Widget build(BuildContext context) {
    final bookProvider = Provider.of<BookProvider>(context, listen: false);
    final authorProvider = Provider.of<AuthorProvider>(context, listen: false);
    final categoryProvider = Provider.of<CategoryProvider>(context, listen: false);

    // Avoiding 'Null check operator used on a null value'
    List<Object> authorsEdit = _editBook == null ? []
        : authorProvider.authors.where(
            (x) => _editBook!.authors.where((y) => y.id == x.id).isNotEmpty
    ).toList();

    List<Object> categoriesEdit = _editBook == null ? []
        : categoryProvider.categories.where(
            (x) => _editBook!.categories.where((y) => y.id == x.id).isNotEmpty
    ).toList();

    return Drawer(
      width: 500,
      child: Form(
        key: _formKey,
        child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 20),
        child: ListView(
          children: [
            const SizedBox(height: 40),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  _editBook == null ? 'Add Book' : 'Edit Book',
                  style: const TextStyle(
                    color: Colors.black,
                    fontWeight: FontWeight.bold,
                    fontSize: 20,
                  ),
                ),
                _editBook != null ?
                Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 5),
                  child: CustomElevatedButton(
                    text: 'View PDF',
                    icon: Icons.remove_red_eye_outlined,
                    color: Colors.greenAccent,
                    onPressed: () async {
                      if (context.mounted) Navigator.push(context, MaterialPageRoute(builder: (context) => BookViewer(book: _oldEditBook!)));
                    },
                  ),
                ) : const SizedBox(),
              ],
            ),
            const SizedBox(height: 20),
            TextFormField(
              controller: titleController,
              decoration: const InputDecoration(labelText: "Title*"),
              validator: (value) {
                if (_editBook == null) {
                  if (value == null || value.isEmpty) {
                    return 'Required';
                  }
                }
                return null;
              },
              onChanged: (value) {
                if (_editBook == null) {
                  _newBook.title = value;
                } else {
                  _editBook!.title = value;
                }
              },
            ),
            const SizedBox(height: 20),
            TextFormField(
              controller: publicationYearController,
              decoration: const InputDecoration(labelText: 'Publication Year'),
              inputFormatters: [
                FilteringTextInputFormatter.digitsOnly,
              ],
              keyboardType: TextInputType.number,
              onChanged: (value) {
                if (_editBook == null) {
                  _newBook.publicationYear = int.parse(value);
                } else {
                  _editBook!.publicationYear = int.parse(value);
                }
              },
            ),
            const SizedBox(height: 20),
            SizedBox(
              child: DropdownButton(
                hint: const Text("Status*"),
                value: bookStatus.name.toUpperCase(),
                items: BookStatus.values.map((var value) {
                  return DropdownMenuItem<String>(
                    value: value.name.toUpperCase(),
                    child: Text(value.name.toUpperCase()),
                  );
                }).toList(),
                onChanged: (value) {
                  if (_editBook == null) {
                    _newBook.
                    bookStatus = BookStatus.values.firstWhere((e) => e.name == value?.toLowerCase());
                  } else {
                    _editBook!.
                    bookStatus = BookStatus.values.firstWhere((e) => e.name == value?.toLowerCase());
                  }
                },
              ),
            ),
            //const SizedBox(height: 20),
            TextFormField(
              controller: descriptionController,
              decoration: const InputDecoration(labelText: 'Description'),
              maxLines: 3,
              onChanged: (value) {
                if (_editBook == null) {
                  _newBook.description = value;
                } else {
                  _editBook!.description = value;
                }
              },
            ),
            const SizedBox(height: 20),
            MultiSelectDialogField(
              buttonText: const Text("Authors*"),
              buttonIcon: const Icon(Icons.person_add_sharp),
              title: const Text("Authors"),
              items: authorProvider.authors
                  .map((e) => MultiSelectItem(e, e.name!)).toList(),
              listType: MultiSelectListType.CHIP,
              searchable: true,
              onConfirm: (values) {
                if (_editBook == null) {
                  _newBook.authors = values.cast<Author>();
                } else {
                  _editBook!.authors = values.cast<Author>();
                }
              },
              initialValue: authorsEdit,
              validator: (value) {
                if (_editBook == null) {
                  if (value == null || value.isEmpty) {
                    return 'Required';
                  }
                }
                return null;
              },
            ),
            const SizedBox(height: 20),
            MultiSelectDialogField(
              buttonText: const Text("Categories*"),
              buttonIcon: const Icon(Icons.toc_outlined),
              title: const Text("Categories"),
              items: categoryProvider.categories
                  .map((e) => MultiSelectItem(e, e.name!)).toList(),
              listType: MultiSelectListType.CHIP,
              searchable: true,
              onConfirm: (values) {
                if (_editBook == null) {
                  _newBook.categories = values.cast<Category>();
                } else {
                  _editBook!.categories = values.cast<Category>();
                }
              },
              initialValue: categoriesEdit,
              validator: (value) {
                if (_editBook == null) {
                  if (value == null || value.isEmpty) {
                    return 'Required';
                  }
                }
                return null;
              },
            ),
            const SizedBox(height: 20),
            SizedBox(
              height: 30,
              width: 20,
              child: Row(
                mainAxisAlignment:  MainAxisAlignment.center,
                children: [
                  ToggleButtons(
                    isSelected: isSelected,
                    onPressed: (int index) {
                      setState(() {
                        for (int buttonIndex = 0; buttonIndex < isSelected.length; buttonIndex++) {
                          if (buttonIndex == index) {
                            isSelected[buttonIndex] = true;
                          } else {
                            isSelected[buttonIndex] = false;
                          }
                        }
                      });
                    },
                    children: const [Text("URL"),Text("PDF")]
                  )
                ]
              )
            ),
            !isSelected[0] ? const SizedBox(height: 20) : const SizedBox(),
            !isSelected[0] ? SizedBox(
              height: 30,
              width: 20,
              child: Row(
                mainAxisAlignment:  MainAxisAlignment.center,
                children: [
                  CustomElevatedButton(
                    text: 'Upload PDF',
                    icon: Icons.upload_file,
                    color: Colors.black,
                    onPressed: () => setFile()
              )]
              )
            ) : const SizedBox(),
            !isSelected[0] ? Text(
              _filename,
              textAlign: TextAlign.center,
            ) : const SizedBox(),
            isSelected[0] ? TextFormField(
              controller: coverUrlController,
              decoration: const InputDecoration(labelText: 'Image/Cover Link'),
              onChanged: (value) {
                if (_editBook == null) {
                  _newBook.coverUrl = value;
                } else {
                  _editBook!.coverUrl = value;
                }
              },
            ) : const SizedBox(),
            isSelected[0] ? TextFormField(
              controller: bookUrlController,
              decoration: const InputDecoration(labelText: 'PDF/Book Link'),
              onChanged: (value) {
                if (_editBook == null) {
                  _newBook.bookUrl = value;
                } else {
                  _editBook!.bookUrl = value;
                }
              },
            ) : const SizedBox(),
            const SizedBox(height: 40),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [

                Expanded(
                  child: CustomElevatedButton(
                    text: _editBook == null ? 'Add Book' : 'Edit',
                    icon: _editBook == null ? Icons.add : Icons.edit,
                    onPressed: () async {
                      if (!_formKey.currentState!.validate()) return;
                      if (_editBook == null) {
                        await bookProvider.addBook(
                            _newBook, _filePickerResult, context, prefs
                        );
                      } else {
                        if (_editBook!.title == _oldEditBook!.title) {
                          _editBook!.title = null;
                        }
                        if (_editBook!.bookUrl == _oldEditBook!.bookUrl) {
                          _editBook!.bookUrl = null;
                        }
                        if (_editBook!.coverUrl == _oldEditBook!.coverUrl) {
                          _editBook!.coverUrl = null;
                        }
                        await bookProvider.editBook(
                            _editBook!, _filePickerResult, context, prefs
                        );
                      }
                      //Navigator.of(context).pop();
                    },
                  ),
                ),
                _editBook != null
                    ? Padding(
                        padding: const EdgeInsets.only(left: 5),
                        child: CustomElevatedButton(
                          text: 'Delete',
                          icon: Icons.delete,
                          color: Colors.red,
                          onPressed: () async {
                            await bookProvider.deleteBook(_editBook!, context, prefs);
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

  Future<void> setFile() async {
    FilePickerResult? result = await FilePicker.platform.pickFiles(
      type: FileType.custom,
      allowedExtensions: ['pdf'],
    );
    if (result != null) {
      setState(() {
        _filename = result.names.first!;
        _filePickerResult = result;
      });
    }
  }
}
