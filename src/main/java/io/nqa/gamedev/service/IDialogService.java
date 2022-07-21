package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Dialog;
import io.nqa.gamedev.model.CustomResponse;

public interface IDialogService {

    String generateGUID();

    String generateGUID_line();

    CustomResponse getById(String databaseId);

    CustomResponse getByDialogId(String dialogId);

    CustomResponse getByProject(String projectDatabaseId);

    CustomResponse getByProjectId(String projectId);

    CustomResponse saveDialog(Dialog dialog, String projectId);

    Dialog saveDialog(Dialog dialog);

    boolean isDialogIdAvailable(String dialogId, String projectId);

    boolean doesDialogLineExist(Dialog dialog, String lineId);
}
