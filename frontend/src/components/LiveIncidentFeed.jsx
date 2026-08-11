function LiveIncidentFeed({ incidents }) {
    return (
        <section className="panel">
            <h2>Live Incident Feed</h2>

            {incidents.length === 0 ? (
                <p>No live incident updates yet.</p>
            ) : (
                <div className="incident-list">
                    {incidents.map((incident) => (
                        <div
                            className="incident-item"
                            key={incident.incidentId}
                        >
                            <strong>{incident.title}</strong>

                            <span>
                {incident.severity}
              </span>

                            <p>
                                {incident.location}
                            </p>
                        </div>
                    ))}
                </div>
            )}
        </section>
    );
}

export default LiveIncidentFeed;