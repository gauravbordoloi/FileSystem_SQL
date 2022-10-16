package com.gaurav.filesystem

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.gaurav.sdk.ElementType
import com.gaurav.sdk.FileSDK

class MainActivity : AppCompatActivity() {

    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fileSDK = FileSDK()
        fileSDK.init(this)

        //CREATE QUERIES

//        Log.e("create('/DE')", fileSDK.fileReader.create("/DE", ElementType.FOLDER).toString())
//        Log.e("create('/FR')", fileSDK.fileReader.create("/FR", ElementType.FOLDER).toString())
//        Log.e("create('/IN')", fileSDK.fileReader.create("/IN", ElementType.FOLDER).toString())
//        Log.e("create('/US')", fileSDK.fileReader.create("/US", ElementType.FOLDER).toString())
//        Log.e("create('/DE')", fileSDK.fileReader.create("/DE", ElementType.FOLDER).toString())
//
//        Log.e(
//            "create('/IN/DL')",
//            fileSDK.fileReader.create("/IN/DL", ElementType.FOLDER).toString()
//        )
//        Log.e(
//            "create('/IN/KA')",
//            fileSDK.fileReader.create("/IN/KA", ElementType.FOLDER).toString()
//        )
//        Log.e(
//            "create('/IN/MH')",
//            fileSDK.fileReader.create("/IN/MH", ElementType.FOLDER).toString()
//        )
//        Log.e(
//            "create('/US/CA')",
//            fileSDK.fileReader.create("/US/CA", ElementType.FOLDER).toString()
//        )
//        Log.e(
//            "create('/US/NY')",
//            fileSDK.fileReader.create("/US/NY", ElementType.FOLDER).toString()
//        )
//        Log.e(
//            "create('/US/TX')",
//            fileSDK.fileReader.create("/US/TX", ElementType.FOLDER).toString()
//        )
//
//        Log.e(
//            "create('/IN/KA/areas')",
//            fileSDK.fileReader.create("/IN/KA/areas", ElementType.FOLDER).toString()
//        )
//        Log.e(
//            "create('/IN/KA/capital.txt')",
//            fileSDK.fileReader.create("/IN/KA/capital.txt", ElementType.FILE).toString()
//        )
//        Log.e(
//            "create('/IN/KA/malls')",
//            fileSDK.fileReader.create("/IN/KA/malls", ElementType.FOLDER).toString()
//        )
//
//        Log.e(
//            "create('/US/CA/google')",
//            fileSDK.fileReader.create("/US/CA/google", ElementType.FOLDER).toString()
//        )
//        Log.e(
//            "create('/US/NY/central-park.png')",
//            fileSDK.fileReader.create("/US/NY/central-park.png", ElementType.FILE).toString()
//        )
//
//        Log.e(
//            "create('/IN/KA/areas/hsr')",
//            fileSDK.fileReader.create("/IN/KA/areas/hsr", ElementType.FOLDER).toString()
//        )
//        Log.e(
//            "create('/IN/KA/areas/indira-nagar')",
//            fileSDK.fileReader.create("/IN/KA/areas/indira-nagar", ElementType.FOLDER).toString()
//        )
//        Log.e(
//            "create('/IN/KA/malls/orion.png')",
//            fileSDK.fileReader.create("/IN/KA/malls/orion.png", ElementType.FILE).toString()
//        )
//        Log.e(
//            "create('/IN/KA/malls/phoenix.png')",
//            fileSDK.fileReader.create("/IN/KA/malls/phoenix.png", ElementType.FILE).toString()
//        )
//        Log.e(
//            "create('/IN/KA/malls/vr.png')",
//            fileSDK.fileReader.create("/IN/KA/malls/vr.png", ElementType.FILE).toString()
//        )
//
//        Log.e(
//            "create('/US/CA/google/address.txt')",
//            fileSDK.fileReader.create("/US/CA/google/address.txt", ElementType.FILE).toString()
//        )
//
//        Log.e(
//            "create('/IN/KA/areas/hsr/pubs')",
//            fileSDK.fileReader.create("/IN/KA/areas/hsr", ElementType.FOLDER).toString()
//        )
//
//        Log.e(
//            "create('/IN/KA/areas/hsr/pubs/hammered.png')",
//            fileSDK.fileReader.create("/IN/KA/areas/hsr/hammered.png", ElementType.FILE).toString()
//        )
//        Log.e(
//            "create('/IN/KA/areas/hsr/pubs/plan-b.png')",
//            fileSDK.fileReader.create("/IN/KA/areas/hsr/plan-b.png", ElementType.FILE).toString()
//        )

        //WRITE QUERIES
//        Log.e(
//            "write('/IN/KA/capital.txt')",
//            fileSDK.fileReader.write("/IN/KA/capital.txt", "Bangalore is cool").toString()
//        )
//
//        //READ QUERIES
//        Log.e(
//            "read('/IN/KA/capital.txt')",
//            fileSDK.fileReader.read("/IN/KA/capital.txt").toString()
//        )

        //SCAN QUERIES
//        Log.e("scan('/')", fileSDK.fileReader.scan("/").toString())
//        Log.e("scan('/IN/KA')", fileSDK.fileReader.scan("/IN/KA").toString())

        //MOVE QUERIES
//        Log.e("move('/IN/KA')", fileSDK.fileReader.move("/IN/KA", "/US").toString())
//        Log.e("scan('/US')", fileSDK.fileReader.scan("/US").toString())
//        Log.e("scan('/IN')", fileSDK.fileReader.scan("/IN").toString())

        //RENAME QUERIES
        Log.e("rename('/IN/KA')", fileSDK.fileReader.rename("/IN/KA", "/US").toString())
        Log.e("scan('/US')", fileSDK.fileReader.scan("/US").toString())

    }

}