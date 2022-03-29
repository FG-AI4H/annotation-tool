import Config from "./Config";
import {callApiWithToken, postApiWithToken} from "../fetch";

class CampaignClient {
    accessToken
    constructor(accessToken) {
        this.accessToken = accessToken;
        this.config = new Config();
    }

    async fetchCampaignList() {
        console.log("Fetching campaigns");

        return callApiWithToken(this.accessToken, this.config.CAMPAIGN_URL)
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


    async fetchCampaignById(campaignId) {
        console.log("Fetching campaign for Id: " + campaignId);

        return callApiWithToken(this.accessToken, `${this.config.CAMPAIGN_URL}/${campaignId}`)
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

    async updateCampaign(campaign) {
        console.log("Updating campaign for Id: " + campaign.campaignUUID);

        return postApiWithToken(this.accessToken, `${this.config.CAMPAIGN_URL}/${campaign.campaignUUID}`,campaign,"PUT")
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

    async addCampaign(campaign) {
        console.log("Creating campaign");

        return postApiWithToken(this.accessToken, this.config.CAMPAIGN_URL,campaign,"POST")
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

    async removeCampaign(campaignUUID) {
        console.log("Deleting campaign for Id: " + campaignUUID);

        return callApiWithToken(this.accessToken, `${this.config.CAMPAIGN_URL}/${campaignUUID}`,"DELETE")
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

export default CampaignClient;
