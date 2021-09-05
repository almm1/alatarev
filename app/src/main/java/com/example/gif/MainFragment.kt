package com.example.gif

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment(private var mode: Int = -1) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val presenter=MainPresenter()
    private var gifs:ArrayList<Gifs> = ArrayList()

    private var cnt = 0
    private var page = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        when(mode){
            3-> presenter.parseJson(presenter.GET("https://developerslife.ru/random?json=true", context), gifs, mode)
            1-> presenter.parseJson(presenter.GET("https://developerslife.ru/top/0?json=true", context), gifs,mode)
            2-> presenter.parseJson(presenter.GET("https://developerslife.ru/hot/0?json=true", context), gifs,mode)
            0-> presenter.parseJson(presenter.GET("https://developerslife.ru/latest/0?json=true", context), gifs,mode)
        }
    }
    private fun button(){
        backButton.setOnClickListener {
            cnt--
            if (cnt == 0) backButton.isEnabled=false
            presenter.show(gifs, cnt)
        }
        nextButton.setOnClickListener {
            cnt++
            backButton.isEnabled=true
            when(mode){
                3->{
                    presenter.parseJson(presenter.GET("https://developerslife.ru/random?json=true", context), gifs, mode)
                    presenter.show(gifs, cnt)
                }
                else->{
                    if (cnt == gifs.size) {
                        page++
                        when (mode){
                            1-> presenter.parseJson(presenter.GET("https://developerslife.ru/top/$page?json=true", context), gifs,mode)
                            2-> presenter.parseJson(presenter.GET("https://developerslife.ru/hot/$page?json=true", context), gifs,mode)
                            0-> presenter.parseJson(presenter.GET("https://developerslife.ru/latest/$page?json=true", context), gifs,mode)
                        }
                    }
                    presenter.show(gifs, cnt)
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        presenter.setParam(imageViewHot,textViewHot,buttons,this)
        backButton.isEnabled=false
        button()
        presenter.show(gifs, cnt)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HotFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}