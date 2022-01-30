class Config {
    SCHEME = process.env.SCHEME ? process.env.SCHEME : "http";
    HOST = process.env.HOST ? process.env.HOST : "localhost";
    PORT = process.env.PORT ? process.env.PORT : "8080";
    MESSAGE_URL = `${this.SCHEME}://${this.HOST}:${this.PORT}/api/v1/messages`;
    USER_URL = `${this.SCHEME}://${this.HOST}:${this.PORT}/api/v1/users`;
    CAMPAIGN_URL = `${this.SCHEME}://${this.HOST}:${this.PORT}/api/v1/campaigns`;
    ACCESS_TOKEN = "accessToken";
    EXPIRATION = "expiration";

    defaultHeaders() {
        return {
            "Content-Type": "application/json",
            Accept: "application/json",
        };
    }

    getExpiration(token) {
        let encodedPayload = token ? token.split(".")[1] : null;
        if (encodedPayload) {
            encodedPayload = encodedPayload.replace(/-/g, "+").replace(/_/g, "/");
            const payload = JSON.parse(window.atob(encodedPayload));
            return payload?.exp ? payload?.exp * 1000 : 0;
        }
        return 0;
    }
}

export default Config;
