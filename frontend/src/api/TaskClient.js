import Config from "./Config";
import {callApiWithToken, postApiWithToken} from "../fetch";

class TaskClient {
    accessToken
    constructor(accessToken) {
        this.accessToken = accessToken;
        this.config = new Config();
    }

    async fetchTaskList() {
        console.log("Fetching tasks");

        return callApiWithToken(this.accessToken, this.config.TASK_URL)
            .then(([response, json]) => {
                if (!response.ok) {
                    return { success: false, error: json };
                }
                return { success: true, data: json };
            })
            .catch((e) => {
                this.handleError(e);
            });
    }

    async fetchMyTaskList() {
        console.log("Fetching my tasks");

        return callApiWithToken(this.accessToken, this.config.TASK_URL + "/me")
            .then(([response, json]) => {
                if (!response.ok) {
                    return { success: false, error: json };
                }
                return { success: true, data: json };
            })
            .catch((e) => {
                this.handleError(e);
            });
    }


    async fetchTaskById(taskId) {
        console.log("Fetching task for Id: " + taskId);

        return callApiWithToken(this.accessToken, `${this.config.TASK_URL}/${taskId}`)
            .then(([response, json]) => {
                if (!response.ok) {
                    return { success: false, error: json };
                }
                return { success: true, data: json };
            })
            .catch((e) => {
                this.handleError(e);
            });
    }

    async addTask(task) {
        console.log("Creating task");

        return postApiWithToken(this.accessToken, this.config.TASK_URL,task,"POST")
            .then(([response]) => {
                if (!response.ok) {
                    return { success: false, error: response };
                }
                return { success: true, data: response };
            })
            .catch((e) => {
                this.handleError(e);
            });
    }

    async updateTask(task) {
        console.log("Updating task");

        return postApiWithToken(this.accessToken, `${this.config.TASK_URL}/${task.taskUUID}`,task,"PUT")
            .then(([response]) => {
                if (!response.ok) {
                    return { success: false, error: response };
                }
                return { success: true, data: response };
            })
            .catch((e) => {
                this.handleError(e);
            });
    }

    async removeTask(taskUUID) {
        console.log("Deleting task for Id: " + taskUUID);

        return callApiWithToken(this.accessToken, `${this.config.TASK_URL}/${taskUUID}`,"DELETE")
            .then(([response, json]) => {
                if (!response.ok) {
                    return { success: false, error: json };
                }
                return { success: true, data: json };
            })
            .catch((e) => {
                this.handleError(e);
            });
    }

    async removeAnnotation(annotationUUID) {
        console.log("Deleting annotation for Id: " + annotationUUID);

        return callApiWithToken(this.accessToken, `${this.config.ANNOTATION_URL}/${annotationUUID}`,"DELETE")
            .then(([response, json]) => {
                if (!response.ok) {
                    return { success: false, error: json };
                }
                return { success: true, data: json };
            })
            .catch((e) => {
                this.handleError(e);
            });
    }


    handleError(error) {
        const err = new Map([
            [TypeError, "There was a problem fetching the response."],
            [SyntaxError, "There was a problem parsing the response."],
            [Error, error.message],
        ]).get(error.constructor);
        console.log(err);
        return err;
    }
}

export default TaskClient;
