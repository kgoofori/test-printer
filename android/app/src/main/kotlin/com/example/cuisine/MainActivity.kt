package com.example.cuisine

import android.R.attr.fontStyle
import android.device.PrinterManager
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel


class MainActivity: FlutterActivity() {
    private val  printManager: PrinterManager = PrinterManager()

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        var yH: Int = 0;
        // Set up MethodChannel
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "com.printer.channel").setMethodCallHandler { call, result ->
            val args = call.arguments<Map<String, Any>>()

            if(call.method == "printText"){
                val rec : Int = printManager.status

                if(rec == 0) {
                    yH += printManager.drawTextEx(
                            (args?.get("text") ?: "") as String?,
                            (args?.get("x") ?: 5) as Int,
                            (args?.get("y") ?: 5) as Int,
                            (args?.get("width") ?: 384) as Int,
                            (args?.get("height") ?: -1) as Int,
                            "/system/fonts/kaishu.ttf",
                            (args?.get("size") ?: 30) as Int,
                            0,
                            (args?.get("style") ?: 0) as Int,
                            0
                        )

                    result.success("0")
                }else{
                    result.error(rec.toString(), "Please check printer", null);
                }

            }else if(call.method == "printLine"){
                val rec : Int = printManager.status

                if(rec == 0) {
                    yH += printManager.drawText(
                        (args?.get("text") ?: "") as String?,
                        (args?.get("x") ?: 0) as Int,
                        (args?.get("y") ?: yH) as Int,
                        "/system/fonts/kaishu.ttf",
                        (args?.get("size") ?: 30) as Int,
                        (args?.get("bold") ?: false) as Boolean,
                        (args?.get("italic") ?: false) as Boolean,
                        0
                    )
                    result.success("0")
                }else{
                    result.error(rec.toString(), "Please check printer", null);
                }

            }else if(call.method == "printBarCode"){
                val rec : Int = printManager.status

                if(rec == 0) {
                    yH += printManager.drawBarcode(
                        (args?.get("text") ?: "") as String?,
                        (args?.get("x") ?: 25) as Int,
                        (args?.get("y") ?: yH) as Int,
                        (args?.get("type") ?: 1) as Int,
                        3,
                        (args?.get("size") ?: 10) as Int,
                        0,
                    )
                    result.success("0")
                }else{
                    result.error(rec.toString(), "Please check printer", null);
                }

            }else if(call.method == "printQRCode"){
                val rec : Int = printManager.status

                if(rec == 0) {
                    yH += printManager.drawBarcode(
                        (args?.get("text") ?: "") as String?,
                        (args?.get("x") ?: 60) as Int,
                        (args?.get("y") ?: yH) as Int,
                        58,
                        3,
                        (args?.get("size") ?: 100) as Int,
                        0,
                    )
                    result.success("0" )
                }else{
                    result.error(rec.toString(), "Please check printer", null);
                }

            }else if(call.method == "donePrint"){
                yH = 0;
                printManager.open()
                printManager.printPage(0)
                result.success("Is printing")

            }else{
                result.notImplemented();
            }
        }
    }
}
