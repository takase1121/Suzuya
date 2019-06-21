package suzuya;

import suzuya.events.GuildMemberJoin;
import suzuya.events.GuildMessage;
import suzuya.events.GuildVoiceUpdate;
import suzuya.events.Ready;

import javax.security.auth.login.LoginException;
import java.lang.InterruptedException;

class Sortie {
    public static void main(String[] args) throws LoginException, InterruptedException {
        SuzuyaClient suzuya = new SuzuyaClient();
        suzuya.client.addEventListener(
                new Ready(suzuya),
                new GuildMessage(suzuya),
                new GuildVoiceUpdate(suzuya),
                new GuildMemberJoin(suzuya)
        );
        suzuya.client.awaitReady();
    }
}
