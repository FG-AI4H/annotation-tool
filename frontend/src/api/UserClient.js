import Config from "./Config";
import {callApiWithToken, postApiWithToken} from "../fetch";

class UserClient {
    accessToken
    constructor(accessToken) {
        this.accessToken = accessToken;
        this.config = new Config();
    }

    async fetchUserList() {
        console.log("Fetching users");

        return callApiWithToken(this.accessToken, this.config.USER_URL)
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


    async fetchUserById(userId) {
        console.log("Fetching user for Id: " + userId);

        return callApiWithToken(this.accessToken, `${this.config.USER_URL}/${userId}`)
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

    async updateUser(user) {
        console.log("Updating user for Id: " + user.userUUID);

        return postApiWithToken(this.accessToken, `${this.config.USER_URL}/${user.userUUID}`,user,"PUT")
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

    async addUser(user) {
        console.log("Creating user");

        return postApiWithToken(this.accessToken, this.config.USER_URL,user,"POST")
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

export default UserClient;
