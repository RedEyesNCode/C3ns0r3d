package com.meyaoo.chatModule.fragments

import android.Manifest
import com.meyaoo.base.BaseFragmentMain
import com.meyaoo.chatModule.adapter.ContactAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.databinding.DataBindingUtil
import com.meyaoo.R
import java.util.ArrayList
import com.meyaoo.chatModule.model.MContact
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import android.os.Build
import android.content.pm.PackageManager
import android.widget.Toast
import android.provider.ContactsContract
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.core.view.get
import com.meyaoo.databinding.FragmentContactBinding
import com.meyaoo.util.ClickListenerRecyclerContactFragment
import com.meyaoo.util.Constant
import com.meyaoo.util.RecyclerViewItemClickListener
import render.animations.Attention
import render.animations.Render

class ContactFragment : BaseFragmentMain(), ClickListenerRecyclerContactFragment, ContactAdapter.onContactAdapterCallbacks  {
    lateinit var binding: FragmentContactBinding
    lateinit var recyclerViewItemClickListener: RecyclerViewItemClickListener
    private var contactAdapter: ContactAdapter? = null
    private var mParam1: String? = null
    private var mParam2: String? = null
    var mContacts = ArrayList<MContact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(
                activity
            ), R.layout.fragment_contact, container, false
        )


        showContacts()
        return binding.root

    }

    private fun initRecyclerview() {
        val arrayList = contactNames
        binding!!.rvContact.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding!!.rvContact.setHasFixedSize(true)
        binding!!.rvContact.itemAnimator = DefaultItemAnimator()
        contactAdapter = ContactAdapter(activity, arrayList,this)
        binding!!.rvContact.adapter = contactAdapter
        recyclerViewItemClickListener = RecyclerViewItemClickListener(requireContext(),binding.rvContact,this)
        binding.rvContact.addOnItemTouchListener(recyclerViewItemClickListener)


        contactAdapter!!.notifyDataSetChanged()
    }

    private fun showContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requireActivity().checkSelfPermission(
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
        } else {
            initRecyclerview()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showContacts()
            } else {
                Toast.makeText(
                    activity,
                    "Until you grant the permission, we can't display the names",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private val contactNames: ArrayList<MContact>
        private get() {
            val cr = requireActivity().contentResolver
            val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
            if (cursor!!.moveToFirst()) {
                do {
                    @SuppressLint("Range") val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    mContacts.add(MContact(null, name, null,false))
                } while (cursor.moveToNext())
            }
            cursor.close()
            return mContacts
        }

    override fun onClick(view: View?, position: Int) {
        super.onClick(view, position)
        Log.i(Constant.DEV_ASHUTOSH, "onClick: FRAGMENT IS NOTIFIED FROM  RECV GOT POSTION : $position")

    }

    override fun onLongClick(view: View?, position: Int) {
        super.onLongClick(view, position)




        if(!mContacts[position].isSelected){
            mContacts.get(position).isSelected = true
            var render = Render(context)
            render.setAnimation(Attention.Swing(view))
            render.start()
        }


        contactAdapter?.notifyDataSetChanged()



        Log.i(Constant.DEV_ASHUTOSH, "onLongClick: FRAGMENT IS NOTIFIED FROM  RECV GOT POSTION : $position")
    }

    override fun onItemVisibility(position: Int, visibilty: Int) {

        if(View.VISIBLE==visibilty){

            mContacts.get(position).isSelected = false
            contactAdapter?.notifyDataSetChanged()


        }


    }

    companion object {
        private const val PERMISSIONS_REQUEST_READ_CONTACTS = 100
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        fun newInstance(param1: String?, param2: String?): ContactFragment {
            val fragment = ContactFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}