import React, { PureComponent } from 'react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

const data = [
    {
        name: 'March 21',
        annotations: 4000,
        pv: 2400,
        amt: 2400,
    },
    {
        name: 'April 21',
        annotations: 3000,
        pv: 1398,
        amt: 2210,
    },
    {
        name: 'May 21',
        annotations: 2000,
        pv: 9800,
        amt: 2290,
    },
    {
        name: 'June 21',
        annotations: 2780,
        pv: 3908,
        amt: 2000,
    },
    {
        name: 'July 21',
        annotations: 1890,
        pv: 4800,
        amt: 2181,
    },
    {
        name: 'August 21',
        annotations: 2390,
        pv: 3800,
        amt: 2500,
    },
    {
        name: 'September 21',
        annotations: 3490,
        pv: 4300,
        amt: 2100,
    },
];

export default class CampaignChart extends PureComponent {
    static demoUrl = 'https://codesandbox.io/s/simple-area-chart-4ujxw';

    render() {
        return (
            <div className="content c-white">
                <ResponsiveContainer height={300} width={"100%"}>
                <AreaChart
                    width={500}
                    height={400}
                    data={data}
                    margin={{
                        top: 10,
                        right: 30,
                        left: 0,
                        bottom: 0,
                    }}
                >
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="name" />
                    <YAxis />
                    <Tooltip />
                    <Area type="monotone" dataKey="annotations" stroke="#8884d8" fill="#8884d8" />
                </AreaChart>
            </ResponsiveContainer>
            </div>
        );
    }
}
