import { useCallback, useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";
import { useNavigate } from "react-router-dom";
import api from "../api/api";
import SummaryCard from "../components/SummaryCard";
import LiveIncidentFeed from "../components/LiveIncidentFeed";

function DashboardPage() {
    const navigate = useNavigate();

    const [summary, setSummary] = useState({
        totalIncidents: 0,
        reported: 0,
        inProgress: 0,
        resolved: 0,
        critical: 0,
    });

    const [notifications, setNotifications] =
        useState([]);

    const [liveIncidents, setLiveIncidents] =
        useState([]);

    const [connectionStatus, setConnectionStatus] =
        useState("Connecting...");

    const [apiError, setApiError] =
        useState("");

    const loadDashboard = useCallback(async () => {
        try {
            const [
                summaryResponse,
                notificationResponse,
            ] = await Promise.all([
                api.get("/incidents/summary"),
                api.get("/notifications"),
            ]);

            setSummary(
                summaryResponse.data
            );

            setNotifications(
                notificationResponse.data
            );

            /*
             * IMPORTANT:
             * If both API calls succeeded,
             * clear any old error message.
             */
            setApiError("");

            console.log(
                "Dashboard loaded successfully."
            );

        } catch (error) {

            const status =
                error.response?.status;

            const url =
                error.config?.url;

            console.error(
                "Dashboard API failed:",
                status,
                url,
                error.response?.data
            );

            setApiError(
                `Dashboard API failed: ${
                    status ?? "unknown"
                } on ${
                    url ?? "unknown endpoint"
                }`
            );

            if (status === 401) {
                console.warn(
                    "Authentication failed."
                );
            }
        }
    }, []);

    useEffect(() => {
        const token =
            localStorage.getItem("token");

        if (!token) {
            navigate("/");
            return;
        }

        loadDashboard();

        const client =
            new Client({

                brokerURL:
                    `${window.location.protocol === "https:" ? "wss" : "ws"}://${window.location.host}/ws`,

                reconnectDelay: 5000,

                onConnect: () => {

                    console.log(
                        "WebSocket/STOMP connected."
                    );

                    setConnectionStatus(
                        "LIVE"
                    );

                    client.subscribe(
                        "/topic/incidents",
                        (message) => {

                            const incident =
                                JSON.parse(
                                    message.body
                                );

                            console.log(
                                "Live incident received:",
                                incident
                            );

                            setLiveIncidents(
                                (current) => [
                                    incident,
                                    ...current,
                                ]
                            );

                            loadDashboard();
                        }
                    );
                },

                onWebSocketClose: () => {

                    setConnectionStatus(
                        "Disconnected"
                    );
                },

                onStompError: (frame) => {

                    console.error(
                        "STOMP error:",
                        frame
                    );

                    setConnectionStatus(
                        "WebSocket error"
                    );
                },

                onWebSocketError: (error) => {

                    console.error(
                        "WebSocket error:",
                        error
                    );

                    setConnectionStatus(
                        "WebSocket error"
                    );
                },
            });

        client.activate();

        return () => {
            client.deactivate();
        };

    }, [
        navigate,
        loadDashboard,
    ]);

    const logout = () => {

        localStorage.removeItem(
            "token"
        );

        navigate("/");
    };

    return (
        <div className="dashboard">

            <header className="dashboard-header">

                <div>
                    <h1>
                        Disaster Response Network
                    </h1>

                    <p>
                        Emergency Operations Dashboard
                    </p>
                </div>

                <div className="header-actions">

          <span
              className={
                  connectionStatus ===
                  "LIVE"
                      ? "live-status"
                      : "offline-status"
              }
          >
            ● {connectionStatus}
          </span>

                    <button
                        onClick={logout}
                    >
                        Logout
                    </button>

                </div>

            </header>

            {apiError && (
                <div className="error">
                    {apiError}
                </div>
            )}

            <section className="summary-grid">

                <SummaryCard
                    title="Total Incidents"
                    value={
                        summary.totalIncidents
                    }
                />

                <SummaryCard
                    title="Reported"
                    value={
                        summary.reported
                    }
                />

                <SummaryCard
                    title="In Progress"
                    value={
                        summary.inProgress
                    }
                />

                <SummaryCard
                    title="Resolved"
                    value={
                        summary.resolved
                    }
                />

                <SummaryCard
                    title="Critical"
                    value={
                        summary.critical
                    }
                />

            </section>

            <div className="dashboard-grid">

                <LiveIncidentFeed
                    incidents={
                        liveIncidents
                    }
                />

                <section className="panel">

                    <h2>
                        Notifications
                    </h2>

                    {notifications.length === 0 ? (

                        <p>
                            No notifications.
                        </p>

                    ) : (

                        <div className="notification-list">

                            {notifications.map(
                                (notification) => (

                                    <div
                                        className="notification-item"
                                        key={
                                            notification.id
                                        }
                                    >

                                        <strong>
                                            {
                                                notification.type
                                            }
                                        </strong>

                                        <p>
                                            {
                                                notification.message
                                            }
                                        </p>

                                        <small>
                                            {
                                                notification.read
                                                    ? "Read"
                                                    : "Unread"
                                            }
                                        </small>

                                    </div>
                                )
                            )}

                        </div>
                    )}

                </section>

            </div>

        </div>
    );
}

export default DashboardPage;