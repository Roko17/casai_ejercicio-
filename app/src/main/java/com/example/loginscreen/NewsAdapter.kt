package com.example.loginscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.new_row_item.view.*


class NewsAdapter(val allNews: List<NoticiasDataClass>): RecyclerView.Adapter<NewsAdapter.NewsHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.NewsHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return NewsHolder(layoutInflater.inflate(R.layout.new_row_item, parent, false))
    }

    override fun onBindViewHolder(holder: NewsAdapter.NewsHolder, position: Int) {
        holder.render(allNews[position])
    }

    override fun getItemCount(): Int = allNews.size

    class NewsHolder(val view: View): RecyclerView.ViewHolder(view){

        fun render(noticia: NoticiasDataClass){

            view.textViewAutor.text = noticia.autor
            view.textViewTitulo.text = noticia.titulo
            view.textViewContenido.text = noticia.contenido
            view.textViewPublicado.text = noticia.publicado

        }

    }


}