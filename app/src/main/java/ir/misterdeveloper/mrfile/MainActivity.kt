package ir.misterdeveloper.mrfile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import ir.misterdeveloper.mrfile.databinding.ActivityMainBinding
import ir.misterdeveloper.mrfile.fragment.FileFragment
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        //The storage location of the program folders in the internal storage = Android >data >packageName
        val file = getExternalFilesDir(null)!! //null = Android >data >packageName
        val path = file.path

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frameLayoutMain, FileFragment(path))
        transaction.commit()


    }


}