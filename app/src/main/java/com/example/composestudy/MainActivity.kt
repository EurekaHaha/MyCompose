package com.example.composestudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.tutorial.SampleData
import com.example.composestudy.ui.theme.ComposeStudyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeStudyTheme {
                Conversation(SampleData.conversationSample)
//                Surface(modifier = Modifier.fillMaxSize()) {
//                    MessageCard(msg = Message("Android", "Start"))
//                }
            }
        }
    }
}

@Composable
fun MessageCardImage() {
    Image(
        painter = painterResource(R.drawable.ic_launcher_foreground),
        contentDescription = "Contact profile picture",
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
    )
}

@Composable
fun MessageCardTextAuthor(author: String) {
    Text(text = author, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleSmall)
}

@Composable
fun MessageCardTextBody(body: String, isExpanded: Boolean) {
    Text(
        text = body,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(all = 4.dp),
        maxLines =  if (isExpanded) Int.MAX_VALUE else 1,
        textAlign = TextAlign.Start
    )
}

@Composable
//Column中使用.weight方法时需要处于RowScope下 RowScope.FuncName 可以解决此问题
fun RowScope.MessageCardTextColum(msg: Message) {
    var isExpanded by remember { mutableStateOf(false) }
    val surfaceColor by animateColorAsState( if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        label = ""
    )
    Column(modifier = Modifier.clickable { isExpanded = !isExpanded }.weight(1F), horizontalAlignment = if (msg.leftOrRight == 'l') Alignment.Start else Alignment.End,) {
        MessageCardTextAuthor(author = msg.author)
        Spacer(modifier = Modifier.height(6.dp))
        Surface(shape = MaterialTheme.shapes.medium, shadowElevation = 1.dp, color = surfaceColor, modifier = Modifier.animateContentSize().padding(1.dp)) {
            MessageCardTextBody(body = msg.body, isExpanded)
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MessageCard(msg: Message) {

    FlowRow(modifier = Modifier.padding(all = 8.dp).fillMaxWidth(), horizontalArrangement = if (msg.leftOrRight == 'l') Arrangement.Start else Arrangement.End) {
        if (msg.leftOrRight == 'r') {
            MessageCardTextColum(msg)
            Spacer(modifier = Modifier.width(8.dp))
            MessageCardImage()
        } else {
            MessageCardImage()
            Spacer(modifier = Modifier.width(8.dp))
            MessageCardTextColum(msg)
        }
    }
}

@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(messages) { message -> MessageCard(msg = message) }
    }
}

@Preview(name = "Light Mode")
@Composable
fun PreviewConversation() {
    ComposeStudyTheme {
        Conversation(messages = SampleData.conversationSample)
    }
}