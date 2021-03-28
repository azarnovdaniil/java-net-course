package ru.daniilazarnov;

import ru.daniilazarnov.datamodel.AuthorisationPack;

public class CommonAdapterPack {
    private int id;

    public CommonAdapterPack(Pack pack) {
//        id = pack.id;
    }

    public String getPack() {
        switch (id) {
            case 1:
                return new AuthorisationPack().getClass().getName();
            case 2:
//                return P1;
            case 3:
//                return P1;
        }
        return "";
    }
}