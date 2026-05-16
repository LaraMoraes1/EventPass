package com.eventpass.mobile.ui;

import android.content.Context;
import android.content.SharedPreferences;
import com.eventpass.mobile.model.User;

public class Session {
    private static final String PREF = "eventpass_session";

    public static void save(Context context, User user) {
        long id = user.id == null ? -1 : user.id;
        String nome = user.nome == null ? "EventPass" : user.nome;
        String email = user.email == null ? "" : user.email;
        String tipo = user.tipo == null ? "PARTICIPANTE" : user.tipo;
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit()
                .putLong("id", id)
                .putString("nome", nome)
                .putString("email", email)
                .putString("tipo", tipo)
                .apply();
    }

    public static long userId(Context context) { return prefs(context).getLong("id", -1); }
    public static String name(Context context) { return prefs(context).getString("nome", "EventPass"); }
    public static String email(Context context) { return prefs(context).getString("email", ""); }
    public static String type(Context context) { return prefs(context).getString("tipo", "PARTICIPANTE"); }
    public static boolean isAdmin(Context context) { return "ADMIN".equals(type(context)); }
    public static void clear(Context context) { prefs(context).edit().clear().apply(); }
    private static SharedPreferences prefs(Context context) { return context.getSharedPreferences(PREF, Context.MODE_PRIVATE); }
}
