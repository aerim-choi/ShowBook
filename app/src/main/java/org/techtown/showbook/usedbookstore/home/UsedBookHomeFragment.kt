package org.techtown.showbook.usedbookstore.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.techtown.showbook.R
import org.techtown.showbook.databinding.FragmentUsedbookstorehomeBinding
import org.techtown.showbook.usedbookstore.DBKey.Companion.CHILD_CHAT
import org.techtown.showbook.usedbookstore.DBKey.Companion.DB_ARTICLES
import org.techtown.showbook.usedbookstore.DBKey.Companion.DB_USERS

class UsedBookHomeFragment: Fragment(R.layout.fragment_usedbookstorehome) {

    private lateinit var articleAdapter : ArticleAdapter
    private lateinit var articleDB : DatabaseReference
    private lateinit var userDB : DatabaseReference

    private val articleList = mutableListOf<ArticleModel>()
    private val listener = object :ChildEventListener{
        //데이터 집어넣기
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val articleModel = snapshot.getValue(ArticleModel::class.java)
            articleModel ?: return
            articleList.add(articleModel)
            articleAdapter.submitList(articleList)
        }
        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {}

    }
    private var binding: FragmentUsedbookstorehomeBinding ?= null
    private val auth : FirebaseAuth by lazy{
        Firebase.auth
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentUsedbookstorehomeBinding= FragmentUsedbookstorehomeBinding.bind(view)

        binding = fragmentUsedbookstorehomeBinding

        userDB = Firebase.database.reference.child(DB_USERS)

        articleList.clear()
        articleDB = Firebase.database.reference.child(DB_ARTICLES)

        articleAdapter = ArticleAdapter(onItemClicked = { articleModel->
//            if(auth.currentUser!=null) {
//                //로그인을 한 상태
//                if (auth.currentUser?.uid != articleModel.sellerId) {
//                    //채팅 방 만들기
////                    val chatRoom =ChatListItem(
////                        buyerId = auth.currentUser!!.uid,
////                        sellerId = articleModel.sellerId,
////                        itemTitle = articleModel.title,
////                        key = System.currentTimeMillis()
////                    )
//                    userDB.child(auth.currentUser!!.uid)
//                        .child(CHILD_CHAT)
//                        .push()
//                        .setValue(chatRoom)
//
//                    userDB.child(articleModel.sellerId)
//                        .child(CHILD_CHAT)
//                        .push()
//                        .setValue(chatRoom)
//                    Snackbar.make(view, "채팅방이 생성되었습니다. 채팅탭에서 확인해주세요.", Snackbar.LENGTH_LONG).show()
//                }else{
//                    //내가 올린 아이템
//                    Snackbar.make(view,"내가 올린 아이템 입니다.",Snackbar.LENGTH_LONG).show()
//                }
//            }
//            else{//로그인을 안한 상태
//                Snackbar.make(view,"로그인 후 사용해 주세요.",Snackbar.LENGTH_LONG).show()
//            }


        })

        fragmentUsedbookstorehomeBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentUsedbookstorehomeBinding.articleRecyclerView.adapter = articleAdapter

        fragmentUsedbookstorehomeBinding.addFloatingButton.setOnClickListener{
//            context?.let{
//                //로그인 했을 경우만 게시물 작성 가능
//                 if(auth.currentUser != null) {
                    val intent = Intent(requireContext(), AddArticleActivity::class.java)
                    startActivity(intent)
//                }else{
//                    Snackbar.make(view,"로그인 후 사용해 주세요.",Snackbar.LENGTH_LONG).show()
//                }
//            }

        }
        articleDB.addChildEventListener(listener)

    }

    override fun onResume() {
        super.onResume()
        articleAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        articleDB.removeEventListener(listener)
    }
}