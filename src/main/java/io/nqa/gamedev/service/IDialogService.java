package io.nqa.gamedev.service;

import io.nqa.gamedev.model.CustomResponse;

public interface IDialogService {

    CustomResponse getById(String databaseId);

    CustomResponse getByDialogId(String dialogId);

    CustomResponse getByProject(String projectDatabaseId);
}
