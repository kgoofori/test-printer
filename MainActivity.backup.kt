package com.example.cuisine

import io.flutter.embedding.android.FlutterActivity
import android.device.PrinterManager
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {
    private val  printManager: PrinterManager = PrinterManager()
    private var yHeight: Int = 0;

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        printManager.open()
        // Set up MethodChannel
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "com.printer.channel").setMethodCallHandler { call, result ->
            val args = call.arguments<Map<String, Any>>()

            if(call.method == "printText"){
                val rec : Int = printManager.status

                if(rec == 0) {
                    yHeight += printManager.drawTextEx(
                         (args?.get("text") ?: "") as String?,
                        5,
                         yHeight,
                        384,
                        -1,
                        "/system/fonts/kaishu.ttf",
                         (args?.get("size") ?: 30) as Int,
                        0,
                        0,
                        0
                    )
                }
                result.success("printText")

            }else if(call.method == "printBarCode"){
                val rec : Int = printManager.status

                if(rec == 0) {
                    yHeight += printManager.drawBarcode(
                        (args?.get("text") ?: "") as String?,
                        (args?.get("x") ?: 25) as Int,
                        yHeight,
                        (args?.get("type") ?: 1) as Int,
                        3,
                        (args?.get("size") ?: 10) as Int,
                        0,
                    )
                    result.success("printBarCode")
                }

            }else if(call.method == "printQRCode"){
                val rec : Int = printManager.status

                if(rec == 0) {
                    yHeight += printManager.drawBarcode(
                        (args?.get("text") ?: "") as String?,
                        (args?.get("x") ?: 60) as Int,
                        yHeight,
                        58,
                        (args?.get("size") ?: 10) as Int,
                        (args?.get("size") ?: 10) as Int,
                        0,
                    )
                    result.success("printQRCode", )
                }

            }else if(call.method == "donePrint"){
                result.success("is printing...$yHeight")
                printManager.printPage(0)
                printManager.paperFeed(0)
                yHeight = 0

            }else{
                result.notImplemented();
            }
        }
    }
}
