package ir.misterdeveloper.mrfile.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast

import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ir.misterdeveloper.mrfile.R
import ir.misterdeveloper.mrfile.adapter.FileAdapter
import ir.misterdeveloper.mrfile.databinding.DialogAddFolderBinding
import ir.misterdeveloper.mrfile.databinding.DialogDeleteFolderBinding
import ir.misterdeveloper.mrfile.databinding.FragmentFileBinding
import java.io.File
import java.text.ParsePosition


class FileFragment(val path: String) : Fragment(), FileAdapter.FileEvent {

    lateinit var binding: FragmentFileBinding
    lateinit var adapter: FileAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFileBinding.inflate(layoutInflater)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ourFile = File(path)
        binding.textViewNameFile.text = ourFile.name + " >"

        if (ourFile.isDirectory) {

            val listOfFiles = arrayListOf<File>()
            listOfFiles.addAll(ourFile.listFiles()!!)

            adapter = FileAdapter(listOfFiles, this)
            binding.recyclerMain.adapter = adapter
            binding.recyclerMain.layoutManager = LinearLayoutManager(context)

            if (listOfFiles.isEmpty()) {

                binding.imageView2.visibility = View.VISIBLE
                binding.recyclerMain.visibility = View.GONE

            } else {
                binding.imageView2.visibility = View.GONE
                binding.recyclerMain.visibility = View.VISIBLE

            }


        }

        binding.buttonAddFolder.setOnClickListener {
            createNewFolder()

        }


    }

    private fun createNewFolder() {

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_folder)
        val addFolderBinding = DialogAddFolderBinding.inflate(layoutInflater)
        dialog.setContentView(addFolderBinding.root)
        dialog.show()

        addFolderBinding.buttonCreate.setOnClickListener {

            val nameOfNewFolder = addFolderBinding.editTextAddFolder.text.toString()

            if (nameOfNewFolder.isNotEmpty()) {


                val newFile = File(path, nameOfNewFolder)
                if (!newFile.exists()) {
                    if (newFile.mkdir()) {

                        dialog.dismiss()
                        adapter.addNewFolder(newFile)
                        binding.recyclerMain.scrollToPosition(0)
                        binding.recyclerMain.visibility = View.VISIBLE
                        binding.imageView2.visibility = View.GONE
                    }
                } else {
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "This folder exists", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                dialog.dismiss()
                Toast.makeText(
                    requireContext(),
                    "The folder name must not be empty",
                    Toast.LENGTH_SHORT
                )
                    .show()

                dialog.dismiss()

            }
            addFolderBinding.buttonCancel.setOnClickListener {
                dialog.dismiss()
            }

        }
        addFolderBinding.buttonCancel.setOnClickListener { dialog.dismiss() }

        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.Animation_Design_BottomSheetDialog
        dialog.window!!.setGravity(Gravity.BOTTOM)

    }

    override fun onFileClick(file: File, type: String) {

        val intent = Intent(Intent.ACTION_VIEW)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            val fileProvider = FileProvider.getUriForFile(
                requireContext(),
                requireActivity().packageName + ".provider", file
            )

            intent.setDataAndType(fileProvider, type)

        } else {
            intent.setDataAndType(Uri.fromFile(file), type)
        }

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }

    override fun onFolderClick(path: String) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayoutMain, FileFragment(path))
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onLongClicked(file: File, position: Int) {

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_folder)
        val deleteFolderBinding = DialogDeleteFolderBinding.inflate(layoutInflater)
        dialog.setContentView(deleteFolderBinding.root)
        dialog.show()

        deleteFolderBinding.buttonYes.setOnClickListener {

            if (file.exists()) {

                if (file.deleteRecursively()) {

                    adapter.removeFolder(file, position)
                    dialog.dismiss()
                }

            }

        }
        deleteFolderBinding.buttonCancel.setOnClickListener { dialog.dismiss() }



        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.Animation_Design_BottomSheetDialog
        dialog.window!!.setGravity(Gravity.BOTTOM)


    }


}


