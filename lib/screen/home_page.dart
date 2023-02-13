import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class HomePage extends StatefulWidget {
  const HomePage({
    Key? key,
  }) : super(key: key);

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  static const batterylevel = MethodChannel('dev-devrash/battery');
  String getBattery = 'Wating..';
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Material App Bar'),
      ),
      body: Center(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Text('Battery percentage is: $getBattery'),
            const SizedBox(
              height: 20,
            ),
            ElevatedButton(
              child: Text('Get Battery level'),
              onPressed: () async {
                await getBatterylevel();
              },
            ),

          ],
        ),
      ),
    );
  }

  Future getBatterylevel() async {
    final args = {'name': 'dev'};
    final int batteryPercentage =
        await batterylevel.invokeMethod('getBatteryLevel', args);
    getBattery = batteryPercentage.toString();
    setState(() {});
  }


}
