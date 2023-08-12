import 'package:flutter/material.dart';

class CustomSwitch extends StatelessWidget {
  final String text;
  final bool value;
  final void Function(bool) onChanged;
  final int height;

  const CustomSwitch({
    Key? key,
    required this.text,
    this.height = 30,
    required this.value,
    required this.onChanged,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Text(text,
            style: const TextStyle(color: Colors.black, fontSize: 10),
            textAlign: TextAlign.center),
        SizedBox(
            //width: 40,
            height: 30,
            child: FittedBox(
                fit: BoxFit.fill,
                child: Switch(
                    value: value,
                    onChanged: (switchClick) {
                      onChanged(switchClick);
                    })))
      ],
    );
  }
}

class CustomTextButton extends StatelessWidget {
  final String messageText;
  final Color messageColor;
  final String textButton;
  final Color colorButton;

  final void Function() onPressed;

  const CustomTextButton(
      {Key? key,
      required this.messageText,
      this.messageColor = Colors.black,
      required this.textButton,
      this.colorButton = Colors.white,
      required this.onPressed})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Text(messageText,
            style: TextStyle(color: messageColor, fontSize: 10),
            textAlign: TextAlign.center),
        SizedBox(
            //width: 40,
            height: 30,
            child: FittedBox(
                fit: BoxFit.fill,
                child:
                    TextButton(onPressed: onPressed, child: Text(textButton))))
      ],
    );
  }
}

class CustomElevatedButton extends StatelessWidget {
  final String text;
  final IconData icon;
  final Function onPressed;
  final Color? color;
  const CustomElevatedButton(
      {Key? key,
        required this.text,
        required this.onPressed,
        required this.icon,
        this.color})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return ElevatedButton(
      style: ElevatedButton.styleFrom(backgroundColor: color),
      onPressed: () => onPressed(),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(icon),
          const SizedBox(
            width: 10.0,
          ),
          Text(text),
        ],
      ),
    );
  }
}
