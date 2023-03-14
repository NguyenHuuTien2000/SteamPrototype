package com.example.steamprototype.data_op;

import android.app.Activity;

import com.example.steamprototype.entity.Game;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Seedr {
    public Activity activity;
    private ArrayList<String> descriptions = new ArrayList<>();

    public Seedr(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Game> start() {
        ArrayList<Game> gameLists = new ArrayList<>();
        Game game;
        Date date;
        String imagePath;

        //hell

        date = convertDate(2018, 6, 1);
        imagePath = createImageStorage(this.activity, "azur_lane.jpg");
        game = new Game(0, "Azure Lane", "Yostar", "Manjuu, Yongshi", "Strategy", "Mobile", date, 0, 0, imagePath, 500_000,100_000);
        gameLists.add(game);

        date = convertDate(2015, 11, 3);
        imagePath = createImageStorage(this.activity, "nfs.jpg");
        game = new Game(1, "Need for Speed", "Electronic Arts", "Ghost Games", "Racing", "PC", date, 82, 10, imagePath, 78_029,18_918);
        gameLists.add(game);

        date = convertDate(2017, 7, 5);
        imagePath = createImageStorage(this.activity, "cold_water.jpg");
        game = new Game(2, "Cold Waters", "Killerfish Games", "Killerfish Games", "Simulator", "PC", date, 40, 25, imagePath,63_389,13_776);
        gameLists.add(game);

        date = convertDate(2019, 4, 19);
        imagePath = createImageStorage(this.activity, "arknights.jpg");
        game = new Game(3, "Arknights", "Yostar", "Hyper Gryph", "Towerdefense", "Mobile", date, 0, 0, imagePath,441_739,93_626);
        gameLists.add(game);

        date = convertDate(2019, 3, 22);
        imagePath = createImageStorage(this.activity, "sekiro.jpg");
        game = new Game(4, "Sekiro", "FromSoftware", "FromSoftware", "Action", "PC", date, 60, 0, imagePath,350_165,75_352);
        gameLists.add(game);

        date = convertDate(2011, 8, 18);
        imagePath = createImageStorage(this.activity, "portal2.jpg");
        game = new Game(5, "Portal 2", "Valve Corp", "Valve Corp", "Puzzle", "PC", date, 10, 0, imagePath,0,0);
        gameLists.add(game);

        date = convertDate(2020, 9, 28);
        imagePath = createImageStorage(this.activity, "genshin.jpg");
        game = new Game(6, "Genshin Impact", "Mihoyo", "Mihoyo", "Adventure", "PC, Mobile", date, 0, 0, imagePath,5_000_000,1_000_000);
        gameLists.add(game);
        writeDescription();
        for (int i = 0; i < gameLists.size(); i++) {
            gameLists.get(i).setDescription(this.descriptions.get(i));
        }

        return gameLists;
    }

    public String createImageStorage(Activity activity, String imageName) {
        String path = "";
        try {
            String savePath = activity.getApplicationInfo().dataDir + "/images/";
            File file = new File(savePath + imageName);
            if (!file.exists()) {
                InputStream inputStream = activity.getAssets().open(imageName);
                File folder = new File(savePath);
                if (!folder.exists()) {
                    folder.mkdir();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(savePath + imageName);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, length);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
            }
            path = savePath + imageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    public Date convertDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        cal.set(year, month, day);
        return cal.getTime();
    }

    public void writeDescription() {
        String desc;
        desc = "\tIn the middle of each nation's normal training routine, a Joint Military Exercise was enacted. A select few from each nation were chosen to participate in this rigorous event. But, how did this event come to be? Are there other ulterior motives at play?\n" +
                "\n" +
                "\t Taking place in a world where personified battleships from across the globe duke it out, Azur Lane: Crosswave takes the spirit of the mobile game and uses the Unreal Engine to bring its characters to life in a massive 3D world, so fans can see them like they’ve never seen them before. The in-game character designs will also feature cel-shaded anime style 3D graphics that stay true to the original art.\n" +
                "\n" +
                "\t Azur Lane is a side-scrolling shooter created by Shanghai Manjuu and Xiamen Yongshi, originally released in 2017 for iOS and Android platforms. The Shanghai-based publisher, Yostar, published the Japanese and English version of the mobile game, popularizing the game to more mobile users across the world. The game takes place in a world where personified battleships from around the globe engage in side-scrolling shooter gameplay.";
        this.descriptions.add(desc);

        desc = "\tReady to own the streets? Get behind the wheel of iconic cars and floor it through Ventura Bay, a sprawling urban playground. Explore overlapping stories as you build your reputation – and your dream car – and become the ultimate racing icon.";
        this.descriptions.add(desc);

        desc = "\tAfter tracking a Soviet landing force bound for Iceland it is time to plan your attack. Do you silently close in to torpedo the landing ships and escape during the resulting chaos? Or strike with long-range missiles but risk counterattack from the enemy escorts? Have you detected them all, could another submarine be out there listening for you? Has the hunter become the hunted? Will you survive the Cold Waters?\n" +
                "\n" +
                "\tInspired by the 1988 classic “Red Storm Rising”, command a nuclear submarine in a desperate attempt to prevent “mutually assured destruction” when the Cold War gets hot and WWIII begins.\n" +
                "\n" +
                "\tYou will be tasked with intercepting convoys, amphibious landings, insertion missions and battling it out with enemy warships, submarines and aircraft. Thankfully, an arsenal of wire-guided torpedoes, anti-ship and cruise missiles and the occasional SEAL team are on board to keep the Iron Curtain at bay.";
        this.descriptions.add(desc);

        desc = "\tTake on the role of a key member of Rhodes Island, a pharmaceutical company that fights both a deadly infection and the unrest it leaves in its wake. Together with your leader Amiya, you’ll recruit Operators, train them, then assign them to various operations to protect the innocent and resist those who would thrust the world into turmoil.\n" +
                "\n" +
                "\tYour tactics will determine the future of Rhodes Island. Fight for the Dawn!";
        this.descriptions.add(desc);

        desc = "\tCarve your own clever path to vengeance in the critically acclaimed adventure from developer FromSoftware, creators of the Dark Souls series.\n" +
                "\n" +
                "\tIn Sekiro™: Shadows Die Twice you are the 'one-armed wolf', a disgraced and disfigured warrior rescued from the brink of death. Bound to protect a young lord who is the descendant of an ancient bloodline, you become the target of many vicious enemies, including the dangerous Ashina clan. When the young lord is captured, nothing will stop you on a perilous quest to regain your honor, not even death itself.";
        this.descriptions.add(desc);

        desc = "\tPortal 2 draws from the award-winning formula of innovative gameplay, story, and music that earned the original Portal over 70 industry accolades and created a cult following.\n" +
                "\n" +
                "\tThe single-player portion of Portal 2 introduces a cast of dynamic new characters, a host of fresh puzzle elements, and a much larger set of devious test chambers. Players will explore never-before-seen areas of the Aperture Science Labs and be reunited with GLaDOS, the occasionally murderous computer companion who guided them through the original game.\n" +
                "\n" +
                "\tThe game’s two-player cooperative mode features its own entirely separate campaign with a unique story, test chambers, and two new player characters. This new mode forces players to reconsider everything they thought they knew about portals. Success will require them to not just act cooperatively, but to think cooperatively.";
        this.descriptions.add(desc);

        desc = "\tGenshin Impact là một game nhập vai phiêu lưu thế giới mở. Bạn sẽ khám phá một thế giới giả tưởng có tên là \"Lục địa Teyvat\" trong trò chơi.\n" +
                "\n" +
                "\tTrong thế giới rộng lớn này, bạn có thể du hành qua bảy vương quốc, gặp gỡ những người bạn đồng hành với những tính cách và năng lực độc đáo khác nhau, cùng chiến đấu chống lại kẻ thù mạnh và bước vào con đường tìm kiếm người thân, hoặc bạn có thể đi lang thang không mục đích và đắm mình trong một thế giới đầy sức sống. Hãy để sự tò mò thúc đẩy bản thân khám phá mọi bí mật trong thế giới này nào...\n" +
                "\n" +
                "\tCho đến khi bạn đoàn tụ với người thân và chứng kiến sự lắng đọng của mọi thứ ở điểm đến cuối cùng.";
        this.descriptions.add(desc);
    }
}
