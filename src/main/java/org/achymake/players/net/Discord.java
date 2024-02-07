package org.achymake.players.net;

import org.achymake.players.Players;
import org.bukkit.configuration.file.FileConfiguration;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public record Discord(Players plugin) {
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    public void send(String username, String text) {
        if (getConfig().getBoolean("discord.enable")) {
            try {
                JSONObject json = new JSONObject();
                json.put("username", username);
                json.put("content", text);
                URL url = new URL(getConfig().getString("discord.webhook"));
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.addRequestProperty("Content-Type", "application/json");
                connection.addRequestProperty("User-Agent", "Java-Discord-BY-Gelox_");
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                OutputStream stream = connection.getOutputStream();
                stream.write(json.toString().getBytes());
                stream.flush();
                stream.close();
                connection.getInputStream().close();
                connection.disconnect();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private class JSONObject {
        private final HashMap<String, Object> map = new HashMap<>();
        void put(String key, Object value) {
            if (value != null) {
                map.put(key, value);
            }
        }
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            Set<Map.Entry<String, Object>> entrySet = map.entrySet();
            builder.append("{");
            int i = 0;
            for (Map.Entry<String, Object> entry : entrySet) {
                Object value = entry.getValue();
                builder.append(quote(entry.getKey())).append(":");
                if (value instanceof String) {
                    builder.append(quote(String.valueOf(value)));
                } else if (value instanceof Integer) {
                    builder.append(Integer.valueOf(String.valueOf(value)));
                } else if (value instanceof Boolean) {
                    builder.append(value);
                } else if (value instanceof JSONObject) {
                    builder.append(value);
                } else if (value.getClass().isArray()) {
                    builder.append("[");
                    int len = Array.getLength(value);
                    for (int j = 0; j < len; j++) {
                        builder.append(Array.get(value, j).toString()).append(j != len - 1 ? "," : "");
                    }
                    builder.append("]");
                }
                builder.append(++i == entrySet.size() ? "}" : ",");
            }
            return builder.toString();
        }
        private String quote(String string) {
            return "\"" + string + "\"";
        }
    }
}