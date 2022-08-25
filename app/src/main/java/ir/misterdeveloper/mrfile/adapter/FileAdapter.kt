package ir.misterdeveloper.mrfile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.misterdeveloper.mrfile.R
import ir.misterdeveloper.mrfile.databinding.ItemFileBinding
import java.io.File
import java.net.URLConnection

class FileAdapter(val data: ArrayList<File>, val fileEvent: FileEvent) :
    RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    lateinit var itemFileBinding: ItemFileBinding

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(file: File) {

            var fileType = ""
            itemFileBinding.textViewNameFile.text = file.name
            if (file.isDirectory) {
                itemFileBinding.imageViewIconFile.setImageResource(R.drawable.ic_file_folder_icon)
            } else {
                when {


                    isImage(file.path) -> {
                        itemFileBinding.imageViewIconFile.setImageResource(R.drawable.ic_photo_icon)
                        fileType = "image/*"

                    }
                    isVideo(file.path) -> {
                        itemFileBinding.imageViewIconFile.setImageResource(R.drawable.ic_video_icon)
                        fileType = "video/*"


                    }
                    isZip(file.name) -> {
                        itemFileBinding.imageViewIconFile.setImageResource(R.drawable.ic_zip_file_icon)
                        fileType = "application/zip"

                    }

                    else -> {
                        itemFileBinding.imageViewIconFile.setImageResource(R.drawable.ic_file_folder_icon)
                        fileType = "text/plain"

                    }

                }
            }

            itemView.setOnClickListener {

                if (file.isDirectory) {
                    fileEvent.onFolderClick(file.path)
                } else {
                    fileEvent.onFileClick(file, fileType)
                }
            }

            itemView.setOnLongClickListener{

                fileEvent.onLongClicked(file, adapterPosition)
                true
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        itemFileBinding = ItemFileBinding.inflate(layoutInflater, parent, false)
        return FileViewHolder(itemFileBinding.root)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {

        holder.bindView(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun isImage(path: String): Boolean {
        val mimeType: String = URLConnection.guessContentTypeFromName(path)
        return mimeType.startsWith("image")
    }


    fun isVideo(path: String): Boolean {
        val mimeType: String = URLConnection.guessContentTypeFromName(path)
        return mimeType.startsWith("video")
    }

    fun isZip(name: String): Boolean {
        return name.contains(".zip") || name.contains(".rar")
    }

    fun addNewFolder(newFile: File) {

        data.add(0, newFile)
        notifyItemInserted(0)
    }

    fun removeFolder(oldFile: File,position: Int) {

        data.remove(oldFile)
        notifyItemRemoved(position )
    }

    interface FileEvent {

        fun onFileClick(file: File, type: String)
        fun onFolderClick(path: String)
        fun onLongClicked(file: File,position: Int)

    }
}