package com.example.socialmediaapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import java.lang.String
import java.util.*

class AboutMeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me)
        val adsElement = Element()
        val aboutPage: View = AboutPage(this)
                .isRTL(false)
                .setDescription(" Developed with \uD83E\uDDE1 by Anand Solanki ")
                .addItem(Element().setTitle("Version 1.0"))
                .addGroup("CONNECT WITH ME!")
                .addItem(Element().setTitle("anand.work26@gmail.com").setIconDrawable(R.drawable.about_icon_email))
                .addItem(Element().setTitle("linkedin.com/in/anand-solanki").setIconDrawable(R.drawable.ic_linkedin))
                .addItem(Element().setTitle("github.com/anandsolucky").setIconDrawable(R.drawable.ic_github))
//            .addEmail("anand.work26@gmail.com")
                .setImage(R.drawable.anand_photo)
//            .addGitHub("/anandsolucky")
//            .addWebsite("linkedin.com/in/anand-solanki")
                .addItem(createCopyright())
                .create()
        setContentView(aboutPage)
    }

    private fun createCopyright(): Element? {
        val copyright = Element()
        @SuppressLint("DefaultLocale") val copyrightString =
                String.format("Copyright %d @AnandSolanki", Calendar.getInstance().get(Calendar.YEAR))
        copyright.setTitle(copyrightString)
        // copyright.setIcon(R.mipmap.ic_launcher);
        copyright.gravity = Gravity.CENTER
        copyright.onClickListener = View.OnClickListener {
            Toast.makeText(this, copyrightString, Toast.LENGTH_SHORT).show()
        }
        return copyright
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.about_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        val back = R.id.back
        val intent: Intent
        if(itemId == back) {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}