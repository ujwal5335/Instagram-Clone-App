package com.example.instagram_clone_app.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram_clone_app.Adapter.NotificationAdapter
import com.example.instagram_clone_app.Model.Notification
import com.example.instagram_clone_app.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class NotificationsFragment : Fragment()
{
    private var notificationList: List<Notification>? = null
    private var notificationAdapter: NotificationAdapter? =null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)


        var recyclerView: RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_notifications)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)


        notificationList = ArrayList()


        notificationAdapter = NotificationAdapter(requireContext(), notificationList as ArrayList<Notification>)
        recyclerView.adapter = notificationAdapter


        readNotifications()


        return view
    }




    private fun readNotifications()
    {
        val notiRef = FirebaseDatabase.getInstance()
            .reference.child("Notifications")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        notiRef.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(datasnapshot: DataSnapshot)
            {
                if (datasnapshot.exists())
                {
                    (notificationList as ArrayList<Notification>).clear()

                    for (snapshot in datasnapshot.children)
                    {
                        val notification = snapshot.getValue(Notification::class.java)

                        (notificationList as ArrayList<Notification>).add(notification!!)
                    }

                    Collections.reverse(notificationList)
                    notificationAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}
