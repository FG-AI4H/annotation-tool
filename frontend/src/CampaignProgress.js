import React from "react";
import {Bar, BarChart, Label, LabelList, ResponsiveContainer, Tooltip, XAxis, YAxis} from "recharts";

const renderCustomizedLabel = (props) => {
    const { content, ...rest } = props;

    return <Label {...rest} fontSize="12" fill="#FFFFFF" fontWeight="Bold" />;
};

export default class CampaignProgress extends React.Component {
    render() {
        const data = [
            { name: "Annotation", completed: 19550, failed: 335, inprogress: 453, remaining: 6000 },

        ];

        return (
            <div className=" c-white">
                <ResponsiveContainer height={100} width={"100%"}>
                    <BarChart
                        layout="vertical"
                        data={data}
                        margin={{ left: 0, right: 0 }}
                        stackOffset="expand"
                    >
                        <XAxis hide type="number" />
                        <YAxis
                            type="category"
                            dataKey="name"
                            stroke="#FFFFFF"
                            fontSize="12"
                        />
                        <Tooltip />
                        <Bar dataKey="completed" fill="#82ba7f" stackId="a">
                            <LabelList
                                dataKey="completed"
                                position="center"
                                content={renderCustomizedLabel}
                            />
                        </Bar>
                        <Bar dataKey="failed" fill="#dd7876" stackId="a">
                            <LabelList
                                dataKey="failed"
                                position="center"
                                content={renderCustomizedLabel}
                            />
                        </Bar>

                        <Bar dataKey="inprogress" fill="#76a8dd" stackId="a">
                            <LabelList
                                dataKey="inprogress"
                                position="center"
                                content={renderCustomizedLabel}
                            />
                        </Bar>
                        <Bar dataKey="remaining" fill="#aeaeae" stackId="a">
                            <LabelList
                                dataKey="remaining"
                                position="center"
                                content={renderCustomizedLabel}
                            />
                        </Bar>
                    </BarChart>
                </ResponsiveContainer>
            </div>
        );
    }
}
