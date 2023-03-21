package tech.skot.librarires.video

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.delay
import org.junit.Before
import org.junit.Test
import tech.skot.core.SKLog
import tech.skot.core.components.inputs.SKButtonViewProxy
import tech.skot.core.di.*
import tech.skot.libraries.video.SKAudioVC
import tech.skot.libraries.video.SKAudioViewProxy
import tech.skot.view.tests.SKTestView
import tech.skot.view.tests.testComponent

class TestViewSKAudio: SKTestView() {




    @Before
    fun injection() {
        injector = BaseInjector(ApplicationProvider.getApplicationContext(), listOf(
            module {
                single<Context> { androidApplication }
            }
        ))
    }

    val track1 = SKAudioVC.Track(
        title = "track1",
        url = "https://sounds-mp3.com/mp3/0011247.mp3",
        image = null
    )
    val track2 = SKAudioVC.Track(
        title = "track2",
        url = "https://sounds-mp3.com/mp3/0011272.mp3",
        image = null
    )
    val track3 = SKAudioVC.Track(
        title = "track3",
        url = "https://sounds-mp3.com/mp3/0011244.mp3",
        image = null
    )

    @Test
    fun testOnDurations() {

        val proxyAudio = SKAudioViewProxy(get())

        val proxyButton = SKButtonViewProxy(labelInitial = "Coucou")
        testComponent(proxyButton) {


            proxyAudio.trackList = listOf(track1, track2, track3)
//            proxyAudio.onCurrentTrack = {
//                SKLog.d("Playing $it")
//            }

            proxyAudio.onDurations = {
                SKLog.d("durations: $it")
            }
            proxyAudio.playing = true

            delay(4000)
            proxyAudio.playing = false
            delay(2000)
            proxyAudio.playing = true
        }

    }


    @Test
    fun testOnState() {
        val proxyAudio = SKAudioViewProxy(get())

        val proxyButton = SKButtonViewProxy(labelInitial = "Coucou")
        testComponent(proxyButton) {


            proxyAudio.trackList = listOf(track1, track2, track3)

            proxyAudio.onState = {
                SKLog.d("staet: $it")
            }
            proxyAudio.playing = true

            delay(4000)
            proxyAudio.playing = false
            delay(2000)
            proxyAudio.playing = true
        }

    }

    @Test
    fun testSetCurrent() {
        val proxyAudio = SKAudioViewProxy(get())

        val proxyButton = SKButtonViewProxy(labelInitial = "Coucou")
        testComponent(proxyButton) {


            proxyAudio.trackList = listOf(track1, track2, track3)

            proxyAudio.onState = {
                SKLog.d("state: $it")
            }
            proxyAudio.playing = true

            delay(4000)

            proxyAudio.setCurrentTrack(track3)
            delay(3000)
            proxyAudio.setCurrentTrack(track2)
        }
    }



}